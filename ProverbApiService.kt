package com.example.kaagada.data.remote

import retrofit2.http.GET

interface ProverbApiService {

    @GET("api/v2/entries/en/hello")
    suspend fun getSample(): List<Any>
}