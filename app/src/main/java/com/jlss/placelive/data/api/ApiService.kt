package com.jlss.placelive.data.api

import com.jlss.placelive.model.Place
import com.jlss.placelive.model.ResponseListDto
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("places")
    suspend fun getPlaces(): ResponseListDto<Place>
    // ✅ Wrapped inside Response<ResponseListDto<Place>> to match backend DTO

    @POST("places")
    suspend fun addPlace(@Body place: Place): Response<Place>
    // ✅ No changes, returns newly created Place

    @DELETE("places/{id}")
    suspend fun deletePlace(@Path("id") id: Int): Response<Unit>
    // ✅ Response<Unit> (better than Void in Kotlin)
}
