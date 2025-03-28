package com.jlss.placelive


import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jlss.placelive.sync.GeofenceSyncWorker
import java.util.concurrent.TimeUnit

class PlaceLiveApp : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleGeofenceSync(this)
    }
}

fun scheduleGeofenceSync(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<GeofenceSyncWorker>(12, TimeUnit.HOURS)
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "GeofenceSync",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
