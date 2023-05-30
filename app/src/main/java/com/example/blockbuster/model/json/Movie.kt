package com.example.blockbuster.model.json

data class Movie(
    val genre_ids: List<Int>,
    val title: String,
): java.io.Serializable {
    var quantity = 0
}

data class Movies(val results: List<Movie>)