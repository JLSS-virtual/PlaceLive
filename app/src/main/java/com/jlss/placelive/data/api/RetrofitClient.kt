package com.jlss.placelive.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * # RetrofitClient - Manages API Calls with Retrofit
 * ---
 * **Purpose:**
 * - This class provides Retrofit instances to interact with the **PlaceLive Geofencing API**.
 * - It ensures a consistent way to call API endpoints while maintaining **code reusability**.
 *
 * **How It Works:**
 * - Uses a **base URL** for API requests.
 * - Defines functions to create API services (`PlaceApi`, `GeofenceApi`).
 * - Uses `GsonConverterFactory` to handle JSON serialization/deserialization.
 *
 * **Why Use Retrofit?**
 * - Retrofit simplifies **network communication** in Android.
 * - It automatically converts API responses into **Kotlin data classes**.
 * - Provides a clean and structured way to make **REST API calls**.
 *
 * ---
 * ## 🔹 Usage Example:
 * ```kotlin
 * val retrofitClient = RetrofitClient()
 * val placeApi = retrofitClient.createPlaceApi()
 * val geofenceApi = retrofitClient.createGeofenceApi()
 * ```
 */
class RetrofitClient() {

    /**
     * ## Base URL for API Requests
     * - This is the **root URL** for all network calls.
     * - Change this URL if the backend server location changes.
     */
    val baseUrl =
        "http://rnbaz-2401-4900-51f1-8428-6967-58a-23b9-1824.a.free.pinggy.link/placelive-geofencing/v1/api/"

    /**
     * ## createPlaceApi - Creates an instance of `PlaceApi`
     * ---
     * **Purpose:**
     * - Initializes Retrofit with the `baseUrl`.
     * - Converts JSON responses into Kotlin objects.
     * - Returns an instance of `PlaceApi` for making network calls.
     */
    fun createPlaceApi(): PlaceApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl) // Set base URL
            .addConverterFactory(GsonConverterFactory.create()) // Add JSON converter
            .build()
            .create(PlaceApi::class.java) // Create API instance
    }

    /**
     * ## createGeofenceApi - Creates an instance of `GeofenceApi`
     * ---
     * **Purpose:**
     * - Initializes Retrofit for Geofence-related API calls.
     * - Converts JSON responses into Kotlin objects.
     * - Returns an instance of `GeofenceApi`.
     */
    fun createGeofenceApi(): GeofenceApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl) // Set base URL
            .addConverterFactory(GsonConverterFactory.create()) // Add JSON converter
            .build()
            .create(GeofenceApi::class.java) // Create API instance
    }
}
