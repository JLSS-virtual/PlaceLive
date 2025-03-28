package com.jlss.placelive.sync

import android.content.Context
import android.util.Log
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * **SyncManager** - Handles geofence synchronization between the local database and the remote API.
 *
 * ## **Core Concept**
 * - Ensures geofences in the local database stay updated.
 * - Syncs only if the last sync was more than **12 hours ago** (to optimize network usage).
 * - Fetches geofences from a remote API and updates the Room database.
 *
 * ## **Essence & Logic**
 * - Maintains synchronization efficiency by checking the last sync timestamp.
 * - Prevents unnecessary API calls by enforcing a **12-hour** sync interval.
 * - Retrieves geofences via **Retrofit API client** and stores them in Room database.
 *
 * ## **Technology Stack**
 * - **Kotlin Coroutines**: Ensures asynchronous, non-blocking sync operations.
 * - **Room Database (DAO)**: Stores geofences locally and keeps track of sync time.
 * - **Retrofit (API Client)**: Handles network requests to fetch geofence data.
 *
 * ## **Goal**
 * - Provide an **automatic, periodic sync** mechanism for geofences.
 * - Minimize network overhead by intelligently managing sync intervals.
 * - Ensure data consistency between the app and remote database.
 *
 * ## **What It Proves**
 * - Efficient geofence syncing using Kotlin Coroutines & Room DB.
 * - Optimal API usage by avoiding excessive requests.
 * - Proper exception handling ensures robustness in real-world usage.
 */
class SyncManager(private val context: Context, private val geofenceDao: GeofenceDao) {

    // API service for fetching geofences from the backend
    private val apiService: GeofenceApi = RetrofitClient().createGeofenceApi()

    /**
     * **syncGeofences()** - Synchronizes geofence data between the local database and the server.
     *
     * **Steps:**
     * 1. **Retrieve last sync time** from Room database.
     * 2. **Check if 12 hours** have passed since the last sync.
     * 3. **If enough time has passed**, fetch geofences from API.
     * 4. **If API call is successful**, store geofences in the local database.
     * 5. **Update last sync timestamp** in the database.
     *
     * **Error Handling:**
     * - Logs an error if the API response is null or fails.
     * - Uses a try-catch block to handle unexpected network failures.
     *
     * **Runs on:** `Dispatchers.IO` (for efficient background execution).
     */
    suspend fun syncGeofences() {
        withContext(Dispatchers.IO) {
            val lastSyncTime = geofenceDao.getLastSyncTime() ?: 0L
            val currentTime = System.currentTimeMillis()
            val hoursSinceLastSync = TimeUnit.MILLISECONDS.toHours(currentTime - lastSyncTime)

            if (hoursSinceLastSync >= 12) { // ✅ Ensures sync only when needed
                try {
                    val response = apiService.getGeofence()

                    if (response.isSuccessful) {
                        response.body()?.data?.let { geofences ->
                            geofenceDao.insertAllGeofences(geofences) // ✅ Insert new geofences
                            geofenceDao.updateLastSyncTime(currentTime) // ✅ Save new sync time
                        } ?: Log.e("SyncManager", "Response body is null")
                    } else {
                        Log.e("SyncManager", "API call failed: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("SyncManager", "Sync failed: ${e.message}", e)
                }
            }
        }
    }
}
