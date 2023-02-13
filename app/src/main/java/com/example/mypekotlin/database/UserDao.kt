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
    //if this application will be big and smart
    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUserById(id: UUID): Flow<User?>
    @Query("SELECT * FROM user WHERE login=(:login) AND password=(:password)")
    fun getUserByLogin(login:String, password: String): User?

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT EXISTS (SELECT 1 FROM user WHERE login = (:login))")
    fun isUserExist(login: String): Boolean
}