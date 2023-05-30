package com.example.blockbuster.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blockbuster.model.Repository
import com.example.blockbuster.model.json.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MovieModel"
private const val MAX_QUANTITY = 10

class MovieModel(application: Application) : AndroidViewModel(application) {
    val genres = MutableLiveData<GenreMap>()
    val reverseGenres = MutableLiveData<ReverseGenreMap>()
    val _movies = mutableListOf<Movie>()
    val movies = MutableLiveData<List<Movie>>()

    fun getGenres() {
        Repository.getGenres().enqueue(object : Callback<GenreList> {
            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                Log.d(TAG, "getGenres failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<GenreList>, response: Response<GenreList>) {
                val map = GenreMap()
                map.init(response.body()!!.genres)
                genres.value = map
                val reverseMap = ReverseGenreMap()
                reverseMap.init(response.body()!!.genres)
                reverseGenres.value = reverseMap
            }
        })
    }

    fun getPopular() {
        Repository.getPopular().enqueue(object: Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getPopular failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                _movies.addAll(response.body()!!.results)
                setQuantities()
                movies.value = _movies
                Log.d(TAG, "getPopular called, movies size: ${_movies.size}")
            }
        })
    }

    fun onLongClick(position: Int) {
        _movies.removeAt(position)
        movies.value = _movies
    }

    fun addMovie(movie: Movie) {
        _movies.add(0, movie)
        movies.value = _movies
    }

    fun searchMovie(query: String) {
        val results = mutableListOf<Movie>()
        for (movie in _movies) {
            if (movie.title.contains(query))
                results.add(movie)
        }
        movies.value = results
    }

    private fun setQuantities() {
        for (movie in _movies)
            movie.quantity = (0..MAX_QUANTITY).random()
    }
}