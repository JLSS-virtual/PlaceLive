package com.jlss.placelive.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.jlss.placelive.utility.ListConverter
import com.jlss.placelive.utility.UserRegionConverter

@Entity(tableName = "users")  // Changed from "user" (SQL keyword conflict)
@TypeConverters(UserRegionConverter::class, ListConverter::class)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,  // Changed to String (Firebase/Google UID)

    @ColumnInfo(name = "user_name")
    val name: String,

    @ColumnInfo(name = "user_bio")
    var userBio: String = "",  // Default empty

    @ColumnInfo(name = "is_logged_in")
    var isLoggedIn: Boolean = false,  // Fixed typo ("isLongedIn" → "isLoggedIn")

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "mobile_number")
    val mobileNumber: String,  // Changed from Number to String (for formatting)

    // Keep other fields but fix relationships
    @Embedded
    val userRegion: UserRegion,

    @ColumnInfo(name = "followers")
    var followers: List<Long> = emptyList(),  // Store userID as Long

    @ColumnInfo(name = "following")
    var following: List<Long> = emptyList(),

    @ColumnInfo(name = "close_friends")
    var closeFriends: List<Long> = emptyList(),  // Fixed typo ("close_friend" → "close_friends")

    @ColumnInfo(name = "profile_image_url")
    val profileImageUrl: String = "",  // Default empty (for Google/Facebook images)

    val accountCreatedAt: String = "",  // Change to String
    val lastLoginAt: String = ""      // Change to String
)