package com.example.blockbuster.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"

object Repository {
    private val service: MovieService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(MovieService::class.java)
    }

    fun getPopular(): Call<Movies> = service.getPopular(API_KEY, 1)
}