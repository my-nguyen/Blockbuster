package com.example.blockbuster.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.blockbuster.R
import com.example.blockbuster.databinding.FragmentNewBinding
import com.example.blockbuster.model.json.Movie

class NewFragment : Fragment(R.layout.fragment_new) {
    val args: NewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNewBinding.bind(view)

        val genreMap = args.reverseGenres
        binding.apply {
            save.setOnClickListener {
                val selection = spinner.selectedItem.toString()
                val genre = genreMap.map[selection]!!
                val movie = Movie(genre_ids = listOf(genre), title = title.text.toString())
                setFragmentResult("NGUYEN", bundleOf("movie" to movie))
                findNavController().navigateUp()
            }
        }
    }
}