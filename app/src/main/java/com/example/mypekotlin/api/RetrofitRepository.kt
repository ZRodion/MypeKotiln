package com.example.mypekotlin.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class RetrofitRepository {
    private val myJsonServerApi: MyJsonServerApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            //ScalarsConverterFactory.create()
            .build()

        myJsonServerApi = retrofit.create()
    }

    suspend fun fetchMarkers() = myJsonServerApi.fetchMarkers()
}