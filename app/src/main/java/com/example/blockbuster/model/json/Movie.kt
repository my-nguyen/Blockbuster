package com.example.blockbuster.model.json

const val IMAGE_PREFIX = "https://image.tmdb.org/t/p/w342/"

private const val MAX_QUANTITY = 5
data class Movie(
    val id: Int,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val poster_path: String,
    val title: String,
): java.io.Serializable {
    val quantity = (0..MAX_QUANTITY).random()

    fun getPoster() = "$IMAGE_PREFIX$poster_path"
    fun getBackdrop() = "$IMAGE_PREFIX$backdrop_path"
}

data class Movies(val results: List<Movie>)