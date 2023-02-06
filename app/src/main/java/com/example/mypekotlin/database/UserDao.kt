package com.example.mypekotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mypekotlin.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUser(id: UUID): Flow<User>

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}