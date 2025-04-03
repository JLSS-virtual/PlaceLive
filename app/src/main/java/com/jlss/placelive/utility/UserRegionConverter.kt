package com.jlss.placelive.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.jlss.placelive.model.UserRegion

class UserRegionConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromUserRegion(region: UserRegion): String {
        return gson.toJson(region)
    }

    @TypeConverter
    fun toUserRegion(json: String): UserRegion {
        return gson.fromJson(json, UserRegion::class.java)
    }
}