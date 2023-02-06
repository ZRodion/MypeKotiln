package com.example.mypekotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mypekotlin.model.User

@Database(entities = [User::class], version = 2)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

val migrate_1_2 = object : Migration(1, 2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE User ADD COLUMN photoFileName TEXT"
        )
    }
}