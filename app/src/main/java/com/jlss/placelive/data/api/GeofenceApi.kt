package com.jlss.placelive.data.api

import com.jlss.placelive.model.Geofence
import com.jlss.placelive.model.ResponseDto
import com.jlss.placelive.model.ResponseListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GeofenceApi {

    @GET("geofence")
    suspend fun getGeofence(): Response<ResponseListDto<Geofence>>
    //  Wrapped inside Response<ResponseListDto<Place>> to match backend DTO
    @GET("geofence/{id}")
    suspend fun getGeofenceById(): Response<ResponseDto<Geofence>>
    @POST("geofence")
    suspend fun addGeofence(@Body geofence: Geofence): Response<ResponseDto<Geofence>>
    @POST("geofence")
    suspend fun updateGeofence(@Path("id") id: Int,@Body geofence: Geofence): Response<ResponseDto<Geofence>>
    @DELETE("geofence/{id}")
    suspend fun deleteGeofence(@Path("id") id: Int): Response<ResponseDto<String>>
    // Response<Unit> (better than Void in Kotlin)
    // i changed to match backend data.
}