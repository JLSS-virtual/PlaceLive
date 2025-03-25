package com.jlss.placelive.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ✅ Use class if you want dependency injection
class RetrofitClient() {
    val baseUrl =
        "http://rnlti-2401-4900-7b32-d0f2-bd08-64cd-13c4-d505.a.free.pinggy.link/placelive-geofencing/v1/api/"

    fun createPlaceApi(): PlaceApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceApi::class.java)
    }
    fun createGeofenceApi(): GeofenceApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeofenceApi::class.java)
    }

}
