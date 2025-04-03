package com.jlss.placelive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Geofence")
data class Geofence(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "geofence_id")
    val geofenceId: Long = 0,

    @ColumnInfo(name="place_id")
    val placeId: Long =0,

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,  // Added latitude

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0,

    @ColumnInfo(name = "radius")
    val radius: Double = 0.0,

    @ColumnInfo(name = "last_synced_at")
    var lastSyncedAt: Long? = null


)