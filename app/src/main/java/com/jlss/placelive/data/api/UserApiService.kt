package com.jlss.placelive.data.api


import com.jlss.placelive.model.MobileNumberRequest
import com.jlss.placelive.model.ResponseListDto
import com.jlss.placelive.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("users/by-mobile-numbers")
    suspend fun getUsersByMobileNumbers(@Body request: MobileNumberRequest): Response<ResponseListDto<List<User>>>

    companion object {
        private const val BASE_URL = "https://your-api-url.com/" // Change this

        val api: UserApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApiService::class.java)
        }
    }
}
