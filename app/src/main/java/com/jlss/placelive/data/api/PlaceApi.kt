package com.jlss.placelive.data.api

import com.jlss.placelive.model.Place
import com.jlss.placelive.model.ResponseDto
import com.jlss.placelive.model.ResponseListDto
import retrofit2.Response
import retrofit2.http.*

interface PlaceApi {

    @GET("places")
    suspend fun getPlaces(): Response<ResponseListDto<Place>>
    @GET("place/{id}")
    suspend fun getPlaceById(): Response<ResponseDto<Place>>
    @POST("places")
    suspend fun addPlace(@Body place: Place): Response<ResponseDto<Place>>
    @POST("places/{id}")
    suspend fun addPlace(@Path("id") id : Int , @Body place: Place): Response<ResponseDto<Place>>
    @DELETE("places/{id}")
    suspend fun deletePlace(@Path("id") id: Int):Response<ResponseDto<String>>
}
