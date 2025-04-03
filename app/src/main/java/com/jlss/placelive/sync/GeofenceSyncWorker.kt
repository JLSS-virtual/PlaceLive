package com.jlss.placelive.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jlss.placelive.database.DatabaseInstance

/**
 * **GeofenceSyncWorker** - A background worker for synchronizing geofences.
 *
 * ## **Core Concept**
 * - Uses WorkManager to periodically synchronize geofence data.
 * - Runs asynchronously with `CoroutineWorker`, ensuring efficiency.
 * - Fetches geofences from the local database and syncs them with a remote source.
 *
 * ## **Essence & Logic**
 * - This worker is scheduled to execute in the background.
 * - It retrieves the geofence DAO from the Room database.
 * - Calls `SyncManager` to handle geofence synchronization.
 *
 * ## **Technology Stack**
 * - **Android WorkManager**: Ensures background execution.
 * - **Coroutines (Kotlin)**: Efficient and non-blocking execution.
 * - **Room Database (DAO)**: Handles local geofence storage.
 *
 * ## **Goal**
 * - Keep geofences updated in the local database.
 * - Maintain data consistency between the local and remote sources.
 * - Ensure reliable sync without blocking UI operations.
 *
 * ## **What It Proves**
 * - Implements an efficient geofencing sync mechanism.
 * - Demonstrates background processing using WorkManager and Coroutines.
 * - Ensures application reliability with scheduled background tasks.
 */
class GeofenceSyncWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    /**
     * Performs the geofence synchronization task in the background.
     *
     * **Steps:**
     * 1. Retrieves the geofence DAO from the Room database.
     * 2. Creates an instance of `SyncManager` to handle synchronization.
     * 3. Calls `syncGeofences()` to perform the actual sync.
     * 4. Returns `Result.success()` to indicate successful execution.
     *
     * @return [Result] - Success status after completing the sync operation.
     */
    override suspend fun doWork(): Result {
        val dao = DatabaseInstance.getDatabase(applicationContext).geofenceDao()
        val syncManager = SyncManager(applicationContext, dao)
        syncManager.syncGeofences()
        return Result.success()
    }
}
