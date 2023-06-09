package com.example.blockbuster.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blockbuster.MyApplication
import com.example.blockbuster.R
import com.example.blockbuster.databinding.FragmentMainBinding
import com.example.blockbuster.model.Movie
import com.example.blockbuster.viewmodel.MovieModel
import com.example.blockbuster.viewmodel.MovieModelFactory
import com.example.blockbuster.viewmodel.MoviesAdapter

private const val TAG = "MainFragment"

class MainFragment : Fragment(R.layout.fragment_main) {
    //    private val viewModel by viewModels<MovieModel>()
    private val viewModel: MovieModel by viewModels {
        val application = requireActivity().application as MyApplication
        MovieModelFactory(application.genreRepository, application.movieRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        Log.d(TAG, "onViewCreated")

        viewModel.genres.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "genres count: ${it.size}")
        })

        binding.fab.setOnClickListener {
            setFragmentResultListener(KEY_MOVIE) { _, bundle ->
                val movie = bundle.get("movie") as Movie
                viewModel.addMovie(movie)
            }
            val action = MainFragmentDirections.toNewFragment()
            view.findNavController().navigate(action)
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            val adapter = MoviesAdapter(it,
                object : MoviesAdapter.OnClickListener {
                    override fun onItemClick(position: Int) {
                        setFragmentResultListener(KEY_MOVIE) { _, bundle ->
                            val movie = bundle.get("movie") as Movie
                            viewModel.updateMovie(movie, position)
                        }
                        val action = MainFragmentDirections.toDetailFragment(it[position])
                        view.findNavController().navigate(action)
                    }
                }, object : MoviesAdapter.OnLongClickListener {
                    override fun onItemClick(position: Int) {
                        Toast.makeText(requireContext(), "${it[position].title} deleted", Toast.LENGTH_LONG).show()
                        viewModel.onLongClick(position)
                    }
                })
            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        }

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
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}