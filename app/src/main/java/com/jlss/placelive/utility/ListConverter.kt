package com.jlss.placelive.utility

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLongList(list: List<Long>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toLongList(json: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}