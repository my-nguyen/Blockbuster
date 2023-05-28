package com.example.blockbuster

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String): Call<GenreList>

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Movies>
}