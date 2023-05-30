package com.example.blockbuster.model.json

data class Genre(val id: Int, val name: String)

// hardcode list of genres instead of downloading it from themoviedb.org
val genres = listOf(
    Genre(28, "Action"),
    Genre(12, "Adventure"),
    Genre(16, "Animation"),
    Genre(35, "Comedy"),
    Genre(80, "Crime"),
    Genre(99, "Documentary"),
    Genre(18, "Drama"),
    Genre(10751, "Family"),
    Genre(14, "Fantasy"),
    Genre(36, "History"),
    Genre(27, "Horror"),
    Genre(10402, "Music"),
    Genre(9648, "Mystery"),
    Genre(10749, "Romance"),
    Genre(878, "Science Fiction"),
    Genre(10770, "TV Movie"),
    Genre(53, "Thriller"),
    Genre(10752, "War"),
    Genre(37, "Western")
)

// need this class for 2 purposes:
// 1. transforming from a List to a Map
// 2. passing the Map into DetailActivity
object GenreMap: java.io.Serializable {
    val map = mutableMapOf<Int, String>()

    init {
        for (genre in genres) {
            map[genre.id] = genre.name
        }
    }
}

object ReverseGenreMap: java.io.Serializable {
    val map = mutableMapOf<String, Int>()

    init {
        for (genre in genres) {
            map[genre.name] = genre.id
        }
    }
}