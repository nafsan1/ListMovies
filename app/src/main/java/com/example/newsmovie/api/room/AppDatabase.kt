package com.example.newsmovie.api.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsmovie.model.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao?


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database"
                )
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}