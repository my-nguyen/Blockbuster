package com.example.blockbuster

//import com.google.android.youtube.player.YouTubeBaseActivity
//import com.google.android.youtube.player.YouTubeInitializationResult
//import com.google.android.youtube.player.YouTubePlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.blockbuster.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "DetailActivity-Truong"

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d(TAG, "version: ${Build.VERSION.SDK_INT}")
        val movie = intent.getSerializableExtra("MOVIE") as Movie
        val genreMap = intent.getSerializableExtra("GENRES") as GenreMap
        val API_KEY = "1fca74d1a066b2433a06dea9b96239fe"

        binding.apply {
            val widthPixels = getResources().getDisplayMetrics().widthPixels
            Glide.with(this@DetailActivity).load(movie.getPoster())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}