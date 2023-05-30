package com.example.blockbuster.model

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GenreRepository(private val genreDao: GenreDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allGenres: Flow<List<Genre>> = genreDao.getAllGenres()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(genre: Genre) {
        genreDao.insert(genre)
    }
}