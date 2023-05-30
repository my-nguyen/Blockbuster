package com.example.blockbuster.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Query("SELECT * FROM genre_table ORDER BY name ASC")
    fun getAllGenres(): Flow<List<Genre>>

//    @Query("SELECT id FROM genre_table WHERE name = '")

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(genre: Genre)

    @Query("DELETE FROM genre_table")
    suspend fun deleteAll()
}