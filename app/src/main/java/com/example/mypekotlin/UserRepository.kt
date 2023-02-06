package com.example.mypekotlin

import android.content.Context
import androidx.room.Room
import com.example.mypekotlin.database.UserDao
import com.example.mypekotlin.database.UserDatabase
import com.example.mypekotlin.database.migrate_1_2
import com.example.mypekotlin.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

private const val DATABASE_NAME = "user-database"


class UserRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
    ){

    private val database: UserDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migrate_1_2)
        .build()

    fun getUser(id: UUID): Flow<User> = database.userDao().getUser(id)
    suspend fun addUser(user: User){
        database.userDao().addUser(user)
    }
    fun updateUser(user: User){
        coroutineScope.launch {
            database.userDao().updateUser(user)
        }
    }

    companion object{
        private var INSTANCE: UserRepository? = null
        fun get(): UserRepository{
            return INSTANCE ?: throw IllegalStateException("Instance is null")
        }
        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = UserRepository(context)
            }
        }
    }
}