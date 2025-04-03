package com.jlss.placelive.data.api

import com.jlss.placelive.model.ResponseDto
import com.jlss.placelive.model.ResponseListDto
import com.jlss.placelive.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("user")
    suspend fun getUsers(): Response<ResponseListDto<User>>
    @GET("user/{id}")
    suspend fun getUserById(): Response<ResponseDto<User>>
    @POST("users")
    suspend fun addUser(@Body user: User): Response<ResponseDto<User>>
    @PUT("users/{id}")
    suspend fun updateUsers(@Path("id") id : Int, @Body user: User): Response<ResponseDto<User>>
    @DELETE("users/{id}")
    suspend fun deleteUsers(@Path("id") id: Int): Response<ResponseDto<String>>

    // we need a method that will return the user by users by mobile no **We will use this to get all users that uses our app by calling user service and that will return the user by mobile no list provide so
    // inpput list of mobileno.
    // output list of users which have that mobile no.
    @POST("users/by-mobile-numbers")
    suspend fun getUsersByMobileNumbers(
        @Body request: List<String>
    ): Response<ResponseListDto<User>>

    //TODO we will also suggests users from near places or on the place the user is AS the app is palceLive it will work seamlasly.
//    @GET("users/{userRegion}")// sending region or current place data to the user services to fetch the user
//    suspend fun getUserByRegion(@Path("userRegion") userRegion: UserRegion): Response<ResponseListDto<User>>

}