package com.jlss.placelive.websocket

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.model.Geofence
import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * # WebSocketManager
 *
 * Manages WebSocket connections for real-time geofence updates with automatic reconnection
 * and network resilience.
 *
 * ## Key Features:
 * - Singleton pattern for centralized connection management
 * - Automatic reconnection with exponential backoff
 * - Network availability awareness
 * - Message processing for geofence updates/deletions
 * - Local database synchronization
 *
 * ## Usage:
 * ```kotlin
 * // Initialize
 * val webSocketManager = WebSocketManager.getInstance(context)
 *
 * // Connect
 * webSocketManager.connect()
 *
 * // Disconnect when no longer needed
 * webSocketManager.disconnect()
 * ```
 *
 * ## Flow:
 * 1. Registers network connectivity listener
 * 2. Establishes WebSocket connection
 * 3. Listens for incoming messages
 * 4. Processes updates and maintains local database
 * 5. Handles disconnections and automatic recovery
 *
 * @property context Application context for system services and database access
 * @property gson Gson instance for JSON serialization/deserialization
 */
class WebSocketManager private constructor(
    private val context: Context,
    private val gson: Gson = Gson()
) : ConnectivityManager.NetworkCallback() {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val isReconnecting = AtomicBoolean(false)
    private var shouldReconnect = true
    private val reconnectDelay = ExponentialBackoff(5000L, 60000L)
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val wsUrl = "ws://rnbaz-2401-4900-51f1-8428-6967-58a-23b9-1824.a.free.pinggy.link/placelive-geofencing/v1/ws"

    init {
        registerNetworkCallback()
    }

    /**
     * Registers network connectivity callback to monitor internet availability
     */
    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, this)
    }

    /**
     * Called when network becomes available. Triggers automatic reconnection
     * if needed.
     *
     * @param network The available network
     */
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        if (shouldReconnect) connect()
    }

    /**
     * Establishes WebSocket connection if not already connected.
     *
     * ## Features:
     * - Adds custom protocol header
     * - Configures message listeners
     * - Resets reconnection backoff on success
     *
     * @throws IllegalStateException If called after [disconnect]
     */
    fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url(wsUrl)
            .addHeader("Sec-WebSocket-Protocol", "v1.geofence")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            /**
             * Handles successful connection establishment
             */
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected to $wsUrl")
                isReconnecting.set(false)
                reconnectDelay.reset()

                // Send STOMP CONNECT frame
                val connectFrame = "CONNECT\naccept-version:1.1,1.0\nhost:server\n\n\u0000"
                webSocket.send(connectFrame)

                // Subscribe to the topic "/topic/places"
                val subscribeFrame = "SUBSCRIBE\nid:sub-1\ndestination:/topic/places\n\n\u0000"
                webSocket.send(subscribeFrame)

                Log.d("WebSocket", "Subscribed to /topic/places")
            }


            /**
             * Processes incoming text messages
             */
            override fun onMessage(webSocket: WebSocket, text: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (text.contains("deleted")) {
                        Log.d("WebSocket", "Geofence deleted: $text")
                    } else {
                        Log.d("WebSocket", "Geofence update received: $text")
                    }
                    processMessage(text)
                }
            }


            /**
             * Processes incoming binary messages (converted to text)
             */
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                CoroutineScope(Dispatchers.IO).launch {
                    processMessage(bytes.utf8())
                }
            }

            /**
             * Handles graceful connection closure
             */
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing: $reason")
                webSocket.close(1000, null)
            }

            /**
             * Handles connection failures and initiates recovery
             */
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection error: ${t.message}")
                handleConnectionFailure(t, response)
            }
        })
    }

    /**
     * Handles different types of connection failures
     *
     * @param t The thrown exception
     * @param response Server response (if available)
     */
    private fun handleConnectionFailure(t: Throwable, response: Response?) {
        webSocket = null
        when {
            t is UnknownHostException -> handleHostResolutionError()
            response?.code == 400 -> handle400Error()
            else -> scheduleReconnect()
        }
    }

    /**
     * Handles DNS resolution failures
     */
    private fun handleHostResolutionError() {
        // Placeholder for future host rotation logic
        scheduleReconnect()
    }

    /**
     * Handles HTTP 400 errors with delayed retry
     */
    private fun handle400Error() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(10000)
            connect()
        }
    }

    /**
     * Gracefully disconnects WebSocket and cleans up resources
     */
    fun disconnect() {
        shouldReconnect = false
        webSocket?.close(1000, "User initiated")
        connectivityManager.unregisterNetworkCallback(this)
    }

    /**
     * Schedules reconnection attempt with exponential backoff
     */
    private fun scheduleReconnect() {
        if (!shouldReconnect || isReconnecting.getAndSet(true)) return

        CoroutineScope(Dispatchers.IO).launch {
            val delay = reconnectDelay.nextDelay()
            Log.d("WebSocket", "Scheduling reconnect in ${delay}ms")
            delay(delay)
            connect()
        }
    }

    /**
     * Processes incoming WebSocket messages
     *
     * @param payload Raw message content
     */
    private suspend fun processMessage(payload: String) {
        try {
            if (payload.isBlank()) return

            val json = gson.fromJson(payload, JsonObject::class.java)
            when {
                json.has("deleted") -> handleDelete(json.get("deleted").asLong)
                else -> handleUpsert(payload)
            }
        } catch (e: Exception) {
            Log.e("WebSocket", "Message processing failed", e)
        }
    }

    /**
     * Deletes geofence from local database
     *
     * @param geofenceId ID of geofence to delete
     */
    private suspend fun handleDelete(geofenceId: Long) {
        val geofenceDao = DatabaseInstance.getDatabase(context).geofenceDao()
        geofenceDao.getGeofenceById(geofenceId)?.let {
            geofenceDao.deleteGeofenceById(geofenceId)
            Log.d("WebSocket", "Deleted Geofence ID: $geofenceId")
        }
    }

    /**
     * Inserts or updates geofence in local database
     *
     * @param payload JSON string containing geofence data
     */
    private suspend fun handleUpsert(payload: String) {
        try {
            val geofence = gson.fromJson(payload, Geofence::class.java)
            DatabaseInstance.getDatabase(context).geofenceDao().insertGeofence(geofence)
            Log.d("WebSocket", "Upserted Geofence ID: ${geofence.geofenceId}")
        } catch (e: Exception) {
            Log.e("WebSocket", "Upsert operation failed", e)
        }
    }

    /**
     * ## Exponential Backoff Helper
     *
     * Manages increasing delays between reconnection attempts
     *
     * @property initialDelay Starting delay in milliseconds
     * @property maxDelay Maximum delay in milliseconds
     */
    private class ExponentialBackoff(
        private val initialDelay: Long,
        private val maxDelay: Long
    ) {
        private var currentDelay = initialDelay

        /**
         * Returns next delay duration and updates internal state
         */
        fun nextDelay(): Long {
            val delay = currentDelay
            currentDelay = (currentDelay * 1.5).toLong().coerceAtMost(maxDelay)
            return delay
        }

        /**
         * Resets delay to initial value
         */
        fun reset() {
            currentDelay = initialDelay
        }
    }

    companion object {
        @Volatile private var instance: WebSocketManager? = null

        /**
         * Provides singleton instance of WebSocketManager
         *
         * @param context Application context
         * @return Shared WebSocketManager instance
         */
        fun getInstance(context: Context): WebSocketManager =
            instance ?: synchronized(this) {
                instance ?: WebSocketManager(context.applicationContext).also { instance = it }
            }
    }
}