package com.jlss.placelive.repository

import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.model.Geofence

class GeofenceRepository(private val geofenceDao: GeofenceDao) {

    suspend fun addGeofence(geofence: Geofence) {
        geofenceDao.insertGeofence(geofence)
    }

    suspend fun getGeofence(id: Long): Geofence? {
        return geofenceDao.getGeofenceById(id)
    }

    suspend fun getAllGeofences(): List<Geofence> {
        return geofenceDao.getAllGeofences()
    }

    suspend fun removeGeofence(geofence: Geofence) {
        geofenceDao.deleteGeofence(geofence)
    }

    suspend fun clearGeofences() {
        geofenceDao.clearAllGeofences()
    }
}