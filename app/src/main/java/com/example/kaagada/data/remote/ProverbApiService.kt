package com.example.kaagada.data.remote

import com.example.kaagada.data.model.Proverb
import retrofit2.http.GET

interface ProverbApiService {
    // This is a dummy URL for the exam - in real life, you'd put your endpoint here
    @GET("proverbs")
    suspend fun getProverbs(): List<Proverb>
}