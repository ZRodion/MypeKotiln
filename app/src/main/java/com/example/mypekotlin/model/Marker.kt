package com.example.mypekotlin.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Marker(
    val name: String,
    val latitude: Double,
    val longitude: Double
)