package com.jlss.placelive.data.repository

import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.model.Geofence

class DataRepository(
    private val geofenceApi: GeofenceApi,
    private val geofenceDao: GeofenceDao
) {
    suspend fun fetchDataAndCache(): List<Geofence> {
        val response = geofenceApi.getGeofence()  // Call to API to fetch geofences
        if (response.isSuccessful) {
            val responseListDto = response.body()  // Get the API response body
            if (responseListDto?.success == true && !responseListDto.data.isNullOrEmpty()) {
                // Assuming responseListDto.data is of type List<Geofence>
                // Cast the data to List<Geofence>
                val geofences = responseListDto.data as List<Geofence>
                geofenceDao.insertAllGeofences(geofences)
                return geofences
            }
        }
        // Return cached data if the API call fails
        return geofenceDao.getAllGeofences()
    }

    // Fetch data locally (used when offline)
    suspend fun getCachedData(): List<Geofence> = geofenceDao.getAllGeofences()
}
