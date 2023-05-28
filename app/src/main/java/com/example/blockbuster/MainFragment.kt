package com.example.blockbuster

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blockbuster.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"
private const val TAG = "MainFragment-Truong"

class MainFragment : Fragment(R.layout.fragment_main) {
    private val movies = mutableListOf<Movie>()
    private lateinit var adapter: MoviesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)

        val genres = GenreMap()
        adapter = MoviesAdapter(movies, genres, object : MoviesAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                val action = MainFragmentDirections.toDetailFragment(movies[position], genres)
                view.findNavController().navigate(action)
            }
        })
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())

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

    private fun reload(results: List<Movie>) {
        movies.clear()
        movies.addAll(results)
        adapter.notifyDataSetChanged()
    }
}