package com.example.mypekotlin.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val login: String,
    val password: String,
    val name: String = "",
    val surname: String = "",
    val personInformation: String = "",
    val photoFileName: String? = null
)
