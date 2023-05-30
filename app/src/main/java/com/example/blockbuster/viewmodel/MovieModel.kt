package com.example.blockbuster.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.blockbuster.model.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MovieModel"
private const val MAX_QUANTITY = 10

class MovieModel(val genreRepository: GenreRepository) : ViewModel() {
    val _movies = mutableListOf<Movie>()
    val movies = MutableLiveData<List<Movie>>()
    val genres = genreRepository.allGenres.asLiveData()

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

    fun insertGenre(genre: Genre) = viewModelScope.launch {
        genreRepository.insert(genre)
    }

    private fun setQuantities() {
        for (movie in _movies)
            movie.quantity = (0..MAX_QUANTITY).random()
    }
}

class MovieModelFactory(private val repository: GenreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}