package com.jlss.placelive.data.repository

import android.util.Log
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.model.Geofence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * **DataRepository** - Centralized data management class.
 *
 * ## **Purpose**
 * - Fetches geofence data from **remote API** and **caches** it in a local database.
 * - Provides **offline support** by returning locally stored data if the API call fails.
 *
 * ## **Essence & Logic**
 * - **First attempts API fetch**: If successful, caches data in Room database.
 * - **Handles API failure**: Returns locally cached geofences if API call fails.
 * - **Uses Coroutines (`Dispatchers.IO`)**: Optimized for background execution.
 *
 * ## **Technology Stack**
 * - **Retrofit (GeofenceApi)**: Handles network requests.
 * - **Room (GeofenceDao)**: Manages local database storage.
 * - **Kotlin Coroutines**: Ensures smooth asynchronous operations.
 */
class DataRepository(
    private val geofenceApi: GeofenceApi,
    private val geofenceDao: GeofenceDao
) {

    /**
     * **fetchDataAndCache()** - Fetches geofences from API and caches them locally.
     *
     * - **First, tries fetching fresh data from the API.**
     * - **If successful**, stores the fetched data in the database.
     * - **If API fails**, returns cached data from the local database.
     *
     * @return List of `Geofence` objects (either from API or database)
     */
    suspend fun fetchDataAndCache(): List<Geofence> = withContext(Dispatchers.IO) {
        try {
            val response = geofenceApi.getGeofence() // API call to fetch geofences

            if (response.isSuccessful) {
                val responseListDto = response.body() // ✅ Extract API response body

                if (responseListDto?.success == true && !responseListDto.data.isNullOrEmpty()) {
                    val geofences = responseListDto.data as List<Geofence> // ✅ Safely cast to list

                    // ✅ Cache fetched geofences into the local database
                    geofenceDao.insertAllGeofences(geofences)

                    Log.d("DataRepository", "Fetched ${geofences.size} geofences from API.")
                    return@withContext geofences
                }
            }

            Log.w("DataRepository", "API fetch failed, returning cached data.")
            return@withContext geofenceDao.getAllGeofences() // Return cached data
        } catch (e: Exception) {
            Log.e("DataRepository", "Error fetching data: ${e.message}", e)
            return@withContext geofenceDao.getAllGeofences() // Return cached data on failure
        }
    }

    /**
     * **getCachedData()** - Retrieves locally stored geofence data.
     *
     * @return List of `Geofence` objects from the local database.
     */
    suspend fun getCachedData(): List<Geofence> = withContext(Dispatchers.IO) {
        return@withContext geofenceDao.getAllGeofences()
    }
}
