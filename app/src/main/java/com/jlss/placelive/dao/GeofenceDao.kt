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

    @Query("DELETE FROM geofencing WHERE geofence_id = :id")
    suspend fun deleteGeofenceById(id: Long): Int


    // Delete all Geofences.
    @Query("DELETE FROM geofencing")
    suspend fun clearAllGeofences()
    /**
     * Retrieves the most recent synchronization timestamp.
     * Ensures that the latest timestamp from the `geofencing` table is fetched.
     */
    @Query("SELECT MAX(last_synced_at) FROM geofencing")
    suspend fun getLastSyncTime(): Long?

    /**
     * Updates the last synchronized timestamp for a specific geofence.
     * WARNING: Updates the entire table if not filtered properly.
     */
    @Query("UPDATE geofencing SET last_synced_at = :time WHERE geofence_id = (SELECT geofence_id FROM geofencing ORDER BY last_synced_at DESC LIMIT 1)")
    suspend fun updateLastSyncTime(time: Long)

    @Query("DELETE FROM geofencing WHERE geofence_id = :geofenceId")
    fun deleteGeofence(geofenceId: Int)

}
