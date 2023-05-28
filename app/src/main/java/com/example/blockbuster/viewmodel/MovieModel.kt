package com.example.blockbuster.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.blockbuster.model.Repository
import com.example.blockbuster.model.json.GenreList
import com.example.blockbuster.model.json.GenreMap
import com.example.blockbuster.model.json.Movie
import com.example.blockbuster.model.json.Movies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MovieModel"
class MovieModel(application: Application) : AndroidViewModel(application) {
    val genres = MutableLiveData<GenreMap>()
    val movies = MutableLiveData<List<Movie>>()

    fun getGenres() {
        Repository.instance?.getGenres()!!.enqueue(object : Callback<GenreList> {
            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                Log.d(TAG, "getGenres failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<GenreList>, response: Response<GenreList>) {
                val map = GenreMap()
                map.init(response.body()!!.genres)
                genres.value = map
                getPopular()
            }
        })
    }

    fun getPopular() {
        Repository.instance?.getPopular()!!.enqueue(object: Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getPopular failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                movies.value = response.body()!!.results
            }
        })
    }
}