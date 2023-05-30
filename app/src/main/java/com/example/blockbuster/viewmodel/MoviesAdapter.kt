package com.example.blockbuster.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbuster.databinding.ItemMovieBinding
import com.example.blockbuster.model.json.GenreMap
import com.example.blockbuster.model.json.Movie

class MoviesAdapter(val movies: List<Movie>, val clickListener: OnClickListener, val longClickListener: OnLongClickListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    interface OnClickListener {
        fun onItemClick(position: Int)
    }
    interface OnLongClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                title.text = movie.title
                genres.text = movie.genre_ids.joinToString(", ") { GenreMap.map[it]!! }
                if (movie.quantity > 0) {
                    quantity.visibility = View.VISIBLE
                    quantity.text = movie.quantity.toString()
                    out.visibility = View.INVISIBLE
                } else {
                    quantity.visibility = View.INVISIBLE
                    out.visibility = View.VISIBLE
                }
            }
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                longClickListener.onItemClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder = ItemMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}