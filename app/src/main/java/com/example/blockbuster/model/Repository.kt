package com.example.blockbuster.model

import com.example.blockbuster.model.json.GenreList
import com.example.blockbuster.model.json.Movies
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository private constructor() {
    private val service: MovieService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(MovieService::class.java)
    }

    fun getGenres(): Call<GenreList> = service.getGenres(API_KEY)

    fun getPopular(): Call<Movies> = service.getPopular(API_KEY, 1)

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"

        var instance: Repository? = null
            get() {
                if (field == null) {
                    field = Repository()
                }
                return field
            }
    }
}