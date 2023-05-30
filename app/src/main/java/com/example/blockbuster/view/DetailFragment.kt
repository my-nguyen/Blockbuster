package com.example.blockbuster.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.blockbuster.R
import com.example.blockbuster.databinding.FragmentDetailBinding
import com.example.blockbuster.model.GenreMap

private const val TAG = "DetailFragment"

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)

        Log.d(TAG, "version: ${Build.VERSION.SDK_INT}")
        val movie = args.movie
        binding.apply {
            title.setText(movie.title)
            genres.setText(movie.genre_ids.joinToString(", ") { GenreMap.map[it]!! })
            quantity.setText(movie.quantity.toString())
        }
    }
}