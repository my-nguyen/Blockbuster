package com.example.blockbuster.model

data class Movie(val genre_ids: List<Int> = emptyList(), val title: String, var quantity: Int = 0): java.io.Serializable

data class Movies(val results: List<Movie>)