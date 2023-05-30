package com.example.blockbuster

import android.app.Application
import com.example.blockbuster.model.GenreDatabase
import com.example.blockbuster.model.GenreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { GenreDatabase.getDatabase(this, applicationScope) }
    val genreRepository by lazy { GenreRepository(database.genreDao()) }
}