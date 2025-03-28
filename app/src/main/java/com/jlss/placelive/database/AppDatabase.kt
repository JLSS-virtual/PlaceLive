package com.jlss.placelive.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.model.Geofence

/**
 * **AppDatabase** - Centralized Room Database for local data storage.
 *
 * ## **Purpose**
 * - Provides a **singleton database instance** for managing geofences.
 * - Ensures **thread-safe access** to the Room database.
 * - Acts as an abstraction layer for database operations.
 *
 * ## **Tech Stack**
 * - **Room Database** for persistent local storage.
 * - **Singleton Pattern** for optimized memory usage.
 * - **Kotlin Coroutines** for efficient async database queries.
 */
@Database(entities = [Geofence::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * **DAO Accessor** - Provides access to Geofence DAO
     */
    abstract fun geofenceDao(): GeofenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * **getDatabase()** - Returns the singleton instance of AppDatabase.
         *
         * - Uses **Double-Checked Locking (DCL)** for **thread safety**.
         * - If an instance already exists, it **returns the existing instance**.
         * - Otherwise, it **creates a new database instance**.
         *
         * @param context Application context for creating the database.
         * @return Singleton instance of `AppDatabase`
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "placelive_database"  // ✅ Database name
                ).fallbackToDestructiveMigration() // ✅ Automatically handles version updates
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
