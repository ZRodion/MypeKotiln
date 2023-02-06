package com.example.mypekotlin.api

import com.example.mypekotlin.model.Marker
import retrofit2.Call
import retrofit2.http.GET

interface MyJsonServerApi {
    @GET("BeeWhy/metro/stations")
    suspend fun fetchMarkers(): List<Marker>
}