package com.jlss.placelive.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// ✅ Use class if you want dependency injection
class RetrofitClient(private val baseUrl: String) {

    fun createPlaceApi(): PlaceApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceApi::class.java)
    }
}
