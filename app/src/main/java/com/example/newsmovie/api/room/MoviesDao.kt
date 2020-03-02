package com.example.newsmovie.api.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsmovie.model.Movie

import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movies: Movie): Completable

    @Query("SELECT * FROM movie_favorite where id=:id")
    fun checkMovie(id: Int): Single<List<Movie>>

    @Query("SELECT * FROM movie_favorite ")
    fun allMovie(): Single<List<Movie>>

    @Delete
    fun delete(movie: Movie): Completable
}