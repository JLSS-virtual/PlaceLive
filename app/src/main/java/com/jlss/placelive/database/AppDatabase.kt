package com.jlss.placelive.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.model.Geofence

@Database(entities = [Geofence::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun geofenceDao(): GeofenceDao
}