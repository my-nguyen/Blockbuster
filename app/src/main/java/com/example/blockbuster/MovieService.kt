package com.example.blockbuster

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String): Call<GenreList>

    @GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Movies>

    @GET("movie/popular")
    fun getPopular(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Movies>

    @GET("movie/top_rated")
    fun getTopRated(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Movies>

    @GET("movie/upcoming")
    fun getUpcoming(@Query("api_key") apiKey: String, @Query("page") page: Int): Call<Movies>

    @GET("movie/{movie_id}/videos")
    fun getVideos(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<Videos>

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        val instance: MovieService by lazy {
            val retrofit =
                Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            retrofit.create(MovieService::class.java)
        }
    }
}