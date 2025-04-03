package com.jlss.placelive.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jlss.placelive.model.User

@Dao
interface UserDao {
    // Insert a single user; returns the new row ID.
    @Insert(onConflict = OnConflictStrategy.REPLACE)// annotation for updating data if exists
    suspend fun insertUser(user: User): Long

    // Insert a list of Users and return the new row id.
    @Insert(onConflict = OnConflictStrategy.REPLACE)// on conflict can update autoatically and create if new.
    suspend fun insertAllUsers(users: List<User>): List<Long>

    // Retrieve a user by id.
    @Query("SELECT * FROM users WHERE user_id = :id")
    suspend fun getUser(id: Long): User?

    // Retrieve all Users
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>// retrieving all users.
    // delete a user by id NOTE THIS IS DANGER ZONE IT SHOULD BE SAFE.
    @Query("DELETE FROM users WHERE user_id = :id")
    suspend fun deleteUserById(id: Long): Int
    @Query("DELETE FROM users")
    suspend fun clearAllUser()

}