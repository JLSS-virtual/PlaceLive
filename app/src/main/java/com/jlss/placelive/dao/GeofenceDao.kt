package com.jlss.placelive.dao

import androidx.room.*
import com.jlss.placelive.model.Geofence

@Dao
interface GeofenceDao {

    // Insert a single Geofence; returns the new row ID.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: Geofence): Long

    // Insert a list of Geofences; returns the list of new row IDs.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGeofences(geofences: List<Geofence>): List<Long>

    // Retrieve a Geofence by its ID.
    @Query("SELECT * FROM geofencing WHERE geofence_id = :id")
    suspend fun getGeofenceById(id: Long): Geofence?

    // Retrieve all Geofences.
    @Query("SELECT * FROM geofencing")
    suspend fun getAllGeofences(): List<Geofence>

    // Delete a given Geofence. The parameter must be an instance of Geofence.
    @Delete
    suspend fun deleteGeofence(geofence: Geofence): Int

    // Delete all Geofences.
    @Query("DELETE FROM geofencing")
    suspend fun clearAllGeofences()
}
