package com.jlss.placelive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_region")
data class UserRegion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "region_id")
    val regionId: Long = 0,

    @ColumnInfo(name = "country")
    val country: String = "",

    @ColumnInfo(name = "state")
    val state: String = "",

    @ColumnInfo(name = "city")
    val city: String = "",

    @ColumnInfo(name = "street")
    val street: String = ""
)
