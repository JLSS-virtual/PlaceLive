package com.jlss.placelive


import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jlss.placelive.dao.GeofenceDao
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.data.repository.DataRepository
import com.jlss.placelive.sync.GeofenceSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlaceLiveApp : Application() {

    private lateinit var geofenceDao: GeofenceDao
    private lateinit var geofenceApi: GeofenceApi

    override fun onCreate() {
        super.onCreate()
        scheduleGeofenceSync(this)

//        // Fetch geofence data when app launches
//        // Fetch geofence data when app launches
//        CoroutineScope(Dispatchers.IO).launch {
//            val repository = DataRepository( geofenceApi,geofenceDao)                       ****** we dont need this if we are fetching data with sync too fast. we will think of it later.
//            repository.fetchDataAndCache()
//        }
    }
}

fun scheduleGeofenceSync(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<GeofenceSyncWorker>(10000, TimeUnit.MILLISECONDS)// for testing get the data every 10 sec
        .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "GeofenceSync",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
    Toast.makeText(context, "Sync started", Toast.LENGTH_SHORT).show()
}
