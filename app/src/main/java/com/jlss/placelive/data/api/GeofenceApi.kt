package com.jlss.placelive.data.api

import com.jlss.placelive.model.Geofence
import com.jlss.placelive.model.GeofenceDto
import com.jlss.placelive.model.ResponseDto
import com.jlss.placelive.model.ResponseListDto
import retrofit2.Response
import retrofit2.http.*

interface GeofenceApi {

    @GET("geofence")
    suspend fun getGeofence(): Response<ResponseListDto<Geofence>>

    @GET("geofence/place/{placeId}")
    suspend fun getGeofencesByPlaceId(@Path("placeId") placeId: Long): Response<ResponseListDto<GeofenceDto>>

    @GET("geofence/{id}")
    suspend fun getGeofenceById(@Path("id") geofenceId: Long): Response<ResponseDto<GeofenceDto>>

    @POST("geofence")
    suspend fun addGeofence(@Body geofenceDto: GeofenceDto): Response<ResponseDto<GeofenceDto>>

    @PUT("geofence/{id}")  // ✅ Changed @POST to @PUT for update
    suspend fun updateGeofence(@Path("id") id: Int, @Body geofenceDto: GeofenceDto): Response<ResponseDto<GeofenceDto>>

    @DELETE("geofence/{id}")
    suspend fun deleteGeofence(@Path("id") id: Int): Response<ResponseDto<String>>
}
