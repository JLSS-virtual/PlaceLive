package com.jlss.placelive.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.model.Geofence
import com.jlss.placelive.model.GeofenceDto
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
    suspend fun fetchDataAndCache(context: Context): List<Geofence> = withContext(Dispatchers.IO) {
        try {
           // ok so 1st we shall remove all data fro geofence dao so, because all data comming again with filter. so just store.
            geofenceDao.clearAllGeofences()
            // now lets call the geofenceapi to fetch all data aat once. we will not sending any parameters because we need whole dataa not paginated .
            val response = geofenceApi.getGeofence()
            if (response.isSuccessful) {
                val responseListDto = response.body() // ✅ Extract API response body

                if (responseListDto?.success == true && !responseListDto.data.isNullOrEmpty()) {
                    val geofences = responseListDto.data as List<Geofence> // ✅ Safely cast to list
                    // yes jeet this is possible in kotlin ha aha ha aha "auto type cast detect.


                    // ✅ Cache fetched geofences into the local database
                    geofenceDao.insertAllGeofences(geofences)// inserting meanss update if  exixxts and create if not so.,

                    Log.d("DataRepository", "Fetched ${geofences.size} geofences from API.")
                    Toast.makeText(context, "Data synced.", Toast.LENGTH_SHORT).show()

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
    suspend fun getCachedData(context: Context): List<Geofence> = withContext(Dispatchers.IO) {
        Toast.makeText(context,"Room data using no internet",Toast.LENGTH_SHORT).show()
        return@withContext geofenceDao.getAllGeofences()
    }

    /**
     * **fetchDataAndUpdateCache()** - Fetches data from remote API, deletes existing data, and updates the local Room DB.
     *
     * - Deletes all existing geofence data in the local database to ensure the data is fresh.
     * - Fetches the latest geofence data from the remote API.
     * - Inserts the new geofences into the local database (replaces existing ones).
     *
     * @return List of `Geofence` objects
     */
    suspend fun fetchDataAndUpdateCache(context: Context): List<Geofence> = withContext(Dispatchers.IO) {
        try {
            val response = geofenceApi.getGeofence() // API call to fetch geofences

            if (response.isSuccessful) {
                val responseListDto = response.body() // Extract API response body

                if (responseListDto?.success == true && !responseListDto.data.isNullOrEmpty()) {
                    val geofences = responseListDto.data as List<Geofence> // Safely cast to List<Geofence>

                    // Insert the fetched geofences into the local database
                    geofenceDao.insertAllGeofences(geofences)

                    Log.d("DataRepository", "Successfully synced ${geofences.size} geofences.")
                    Toast.makeText(context,"Data updated.",Toast.LENGTH_SHORT).show()
                    return@withContext geofences
                }
            }

            Log.w("DataRepository", "API fetch failed or no new data, returning empty list.")
            Toast.makeText(context,"Server Side error.",Toast.LENGTH_SHORT).show()
            // If API call fails, return an empty list
            return@withContext emptyList<Geofence>()

        } catch (e: Exception) {
            Log.e("DataRepository", "Error fetching or updating data: ${e.message}", e)
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
            // In case of an error, return an empty list
            return@withContext emptyList<Geofence>()
        }
    }

}
