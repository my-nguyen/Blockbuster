package com.example.blockbuster.view

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blockbuster.R
import com.example.blockbuster.databinding.FragmentMainBinding
import com.example.blockbuster.model.json.Movie
import com.example.blockbuster.viewmodel.MovieModel
import com.example.blockbuster.viewmodel.MoviesAdapter

private const val TAG = "MainFragment"

class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel by viewModels<MovieModel>()

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
                })
            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        }
        viewModel.getGenres()

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // get a hold of the search menu item
                menuInflater.inflate(R.menu.menu_main, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView?

                // listen for the search query
                searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        // reset the search menu item
                        searchView.clearFocus()
                        searchView.setQuery("", false)
                        searchView.isIconified = true
                        searchItem.collapseActionView()
                        viewModel.searchMovie(query)
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}