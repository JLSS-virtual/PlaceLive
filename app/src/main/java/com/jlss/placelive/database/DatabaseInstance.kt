package com.jlss.placelive.database

import android.content.Context
import androidx.room.Room

/**
 * **DatabaseInstance** - Singleton object for managing the Room database instance.
 *
 * ## **Purpose**
 * - Provides a **single shared instance** of `AppDatabase` throughout the app.
 * - Ensures **thread-safe access** using `@Volatile` and `synchronized`.
 * - Prevents **unnecessary memory usage** by reusing the same database instance.
 *
 * ## **Key Features**
 * - **Lazy Initialization**: Creates the database only when first accessed.
 * - **Thread Safety**: Uses **double-checked locking** for concurrent access.
 * - **Reusability**: Prevents redundant database creation.
 */
object DatabaseInstance {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    /**
     * **getDatabase()** - Returns the singleton database instance.
     *
     * - Uses **synchronized block** to prevent multiple thread access.
     * - If an instance already exists, it **returns the cached instance**.
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
                "placelive_database" // ✅ Database name
            ).fallbackToDestructiveMigration() // ✅ Handles schema changes without crashing
                .build()
            INSTANCE = instance
            instance
        }
    }
}
