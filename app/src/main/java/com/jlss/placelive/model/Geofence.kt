package com.jlss.placelive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofencing")
data class Geofence(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "geofence_id")
    val geofenceId: Long = 0,

    @ColumnInfo(name = "radius")
    val radius: Double = 0.0,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = false,

    @ColumnInfo(name = "notification_message")
    val notificationMessage: String? = null,

    @ColumnInfo(name = "entry_count")
    val entryCount: Int = 0,

    @ColumnInfo(name = "exit_count")
    val exitCount: Int = 0,

    @ColumnInfo(name = "last_entry_timestamp")
    val lastEntryTimestamp: String? = null, // Store as String (ISO format)

    @ColumnInfo(name = "last_exit_timestamp")
    val lastExitTimestamp: String? = null,

    @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,  // Added latitude

    @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0
                            // Added longitude
    )
