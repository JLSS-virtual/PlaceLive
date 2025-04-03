package com.jlss.placelive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class GeofenceDto(

    val geofenceId: Long = 0,


    val placeId: Long,


    val radius: Double = 0.0,


    val isActive: Boolean = true,

    val notificationsEnabled: Boolean = false,

    val notificationMessage: String = "",

    val entryCount: Int = 0,

    val exitCount: Int = 0,

    val lastEntryTimestamp: String? = null, // Store as String (ISO format)


    val lastExitTimestamp: String? = null,


    val latitude: Double = 0.0,  // Added latitude


    val longitude: Double = 0.0,


    )

