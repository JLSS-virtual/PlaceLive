package com.jlss.placelive.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.dao.UserDao
import com.jlss.placelive.model.Geofence
import com.jlss.placelive.model.User
import com.jlss.placelive.model.UserRegion
import com.jlss.placelive.utility.ListConverter
import com.jlss.placelive.utility.UserRegionConverter

@Database(
    entities = [
        Geofence::class,
        User::class,          // Added User entity
        UserRegion::class     // Added UserRegion entity
    ],
    version = 4,  // Incremented version
    exportSchema = false
)
@TypeConverters(
    ListConverter::class,
    UserRegionConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun geofenceDao(): GeofenceDao
    abstract fun userDao(): UserDao  // Now properly configured

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "placelive_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(databaseCallback)  // Optional: Add if you need initial setup
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Optional: Add database population callback if needed
        private val databaseCallback = object : RoomDatabase.Callback() {
            // Implement onCreate/onOpen if needed
        }
    }
}