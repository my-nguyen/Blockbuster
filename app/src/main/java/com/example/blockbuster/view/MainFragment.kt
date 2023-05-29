package com.example.blockbuster.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blockbuster.R
import com.example.blockbuster.databinding.FragmentMainBinding
import com.example.blockbuster.model.json.Movie
import com.example.blockbuster.viewmodel.MovieModel
import com.example.blockbuster.viewmodel.MoviesAdapter

private const val TAG = "MainFragment"

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var viewModel: MovieModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)

        binding.fab.setOnClickListener {
            setFragmentResultListener("NGUYEN") { key, bundle ->
                val movie = bundle.get("movie") as Movie
                viewModel.addMovie(movie)
            }
            val action = MainFragmentDirections.toNewFragment(viewModel.reverseGenres.value!!)
            view.findNavController().navigate(action)
        }

        viewModel = ViewModelProvider(this)[MovieModel::class.java]
        viewModel.genres.observe(viewLifecycleOwner) {
            viewModel.getPopular()
        }
        viewModel.movies.observe(viewLifecycleOwner) {
            val adapter = MoviesAdapter(it, viewModel.genres.value!!,
                object : MoviesAdapter.OnClickListener {
                    override fun onItemClick(position: Int) {
                        val action = MainFragmentDirections.toDetailFragment(it[position], viewModel.genres.value!!)
                        view.findNavController().navigate(action)
                    }
                }, object : MoviesAdapter.OnLongClickListener {
                    override fun onItemClick(position: Int) {
                        viewModel.onLongClick(position)
                    }
                }, requireActivity())
            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        }
        viewModel.getGenres()
    }
}