package com.jlss.placelive.data.api

import com.jlss.placelive.model.Geofence
import com.jlss.placelive.model.ResponseDto
import com.jlss.placelive.model.ResponseListDto
import retrofit2.Response
import retrofit2.http.*

interface GeofenceApi {

    @GET("geofence")
    suspend fun getGeofence(): Response<ResponseListDto<Geofence>>

    @GET("geofence/place/{placeId}")
    suspend fun getGeofencesByPlaceId(@Path("placeId") placeId: Long): Response<ResponseListDto<Geofence>>

    @GET("geofence/{id}")
    suspend fun getGeofenceById(@Path("id") geofenceId: Long): Response<ResponseDto<Geofence>>

    @POST("geofence")
    suspend fun addGeofence(@Body geofence: Geofence): Response<ResponseDto<Geofence>>

    @PUT("geofence/{id}")  // ✅ Changed @POST to @PUT for update
    suspend fun updateGeofence(@Path("id") id: Int, @Body geofence: Geofence): Response<ResponseDto<Geofence>>

    @DELETE("geofence/{id}")
    suspend fun deleteGeofence(@Path("id") id: Int): Response<ResponseDto<String>>
}
