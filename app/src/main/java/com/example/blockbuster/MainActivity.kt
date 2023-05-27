package com.example.blockbuster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blockbuster.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"
private const val TAG = "MainActivity-Truong"

class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genres = GenreMap()
        adapter = MoviesAdapter(movies, genres, object : MoviesAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("MOVIE", movies[position])
                intent.putExtra("GENRES", genres)
                startActivity(intent)
            }
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        MovieService.instance.getGenres(API_KEY).enqueue(object : Callback<GenreList> {
            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                Log.d(TAG, "getGenres failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<GenreList>, response: Response<GenreList>) {
                genres.init(response.body()!!.genres)
                getPopular()
            }
        })
    }

    private fun getNowPlaying() {
        MovieService.instance.getNowPlaying(API_KEY, 1).enqueue(object : Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getNowPlaying failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                reload(response.body()!!.results)
            }
        })
    }

    private fun getPopular() {
        MovieService.instance.getPopular(API_KEY, 1).enqueue(object: Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getPopular failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                reload(response.body()!!.results)
            }
        })
    }

    private fun getTopRated() {
        MovieService.instance.getTopRated(API_KEY, 1).enqueue(object: Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getTopRated failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                reload(response.body()!!.results)
            }
        })
    }

    private fun getUpcoming() {
        MovieService.instance.getUpcoming(API_KEY, 1).enqueue(object : Callback<Movies> {
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Log.d(TAG, "getUpcoming failed ${t.printStackTrace()}")
            }

            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                reload(response.body()!!.results)
            }
        })
    }

    private fun reload(results: List<Movie>) {
        movies.clear()
        movies.addAll(results)
        adapter.notifyDataSetChanged()
    }
}