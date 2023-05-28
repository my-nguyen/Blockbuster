package com.example.blockbuster

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.blockbuster.databinding.FragmentDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "DetailFragment"

class DetailFragment : Fragment(R.layout.fragment_detail) {
    val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)

        Log.d(TAG, "version: ${Build.VERSION.SDK_INT}")
        val movie = args.movie!!
        val genreMap = args.genres
        val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"
        binding.apply {
            Glide.with(this@DetailFragment).load(movie.getPoster())
                .into(poster)
            title.setText(movie.title)
            genres.setText(movie.genre_ids.joinToString(", ") { genreMap.map[it]!! })
            quantity.setText(movie.quantity.toString())

            MovieService.instance.getVideos(movie.id, API_KEY).enqueue(object : Callback<Videos> {
                override fun onFailure(call: Call<Videos>, t: Throwable) {
                    Log.d(TAG, "onFailure")
                }

                override fun onResponse(call: Call<Videos>, response: Response<Videos>) {
                    val key = response.body()!!.results[0].key
                    Log.d(TAG, "onResponse, body ${response.body()}")
                }
            })
        }
    }
}