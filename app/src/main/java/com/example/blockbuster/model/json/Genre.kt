package com.example.blockbuster.model.json

data class Genre(val id: Int, val name: String)

// need this class, since the corresponding json object contains a string "genres" and a List<Genre>
data class GenreList(val genres: List<Genre>)

// need this class for 2 purposes:
// 1. transforming from a List to a Map
// 2. passing the Map into DetailActivity
class GenreMap: java.io.Serializable {
    val map = mutableMapOf<Int, String>()

    fun init(genres: List<Genre>) {
        for (genre in genres) {
            map[genre.id] = genre.name
        }
    }
}