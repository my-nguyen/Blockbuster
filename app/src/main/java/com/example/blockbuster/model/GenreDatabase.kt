package com.example.blockbuster.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Genre::class), version = 1, exportSchema = false)
abstract class GenreDatabase : RoomDatabase() {

    abstract fun genreDao(): GenreDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: GenreDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GenreDatabase {
            // if the INSTANCE is null, then create the database. else return it,
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, GenreDatabase::class.java, "genre_database"
                ).addCallback(GenreCallback(scope)).build()
                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    private class GenreCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.genreDao())
                }
            }
        }

        suspend fun populateDatabase(genreDao: GenreDao) {
            genreDao.deleteAll()

            genreDao.insert(Genre(28, "Action"))
            genreDao.insert(Genre(12, "Adventure"))
            genreDao.insert(Genre(16, "Animation"))
            genreDao.insert(Genre(35, "Comedy"))
            genreDao.insert(Genre(80, "Crime"))
            genreDao.insert(Genre(99, "Documentary"))
            genreDao.insert(Genre(18, "Drama"))
            genreDao.insert(Genre(10751, "Family"))
            genreDao.insert(Genre(14, "Fantasy"))
            genreDao.insert(Genre(36, "History"))
            genreDao.insert(Genre(27, "Horror"))
            genreDao.insert(Genre(10402, "Music"))
            genreDao.insert(Genre(9648, "Mystery"))
            genreDao.insert(Genre(10749, "Romance"))
            genreDao.insert(Genre(878, "Science Fiction"))
            genreDao.insert(Genre(10770, "TV Movie"))
            genreDao.insert(Genre(53, "Thriller"))
            genreDao.insert(Genre(10752, "War"))
            genreDao.insert(Genre(37, "Western"))
        }
    }
}
