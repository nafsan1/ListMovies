package com.example.newsmovie.repository

import androidx.lifecycle.LiveData
import com.example.newsmovie.api.Webservices
import com.example.newsmovie.api.myApi
import com.example.newsmovie.api.room.MoviesDao
import com.example.newsmovie.model.Movie

import io.reactivex.Completable
import io.reactivex.Single

class MovieRepository( private val movieDao: MoviesDao?) {

    private var services: Webservices = myApi


    fun getMovie(category:String,apiKey:String) = services.getMovie(category,
         apiKey,
         "en-US")

     fun insert(movie:Movie):Completable{
        return movieDao!!.insert(movie)
    }

    fun delete(movie:Movie):Completable{
        return movieDao!!.delete(movie)
    }

    fun getCheckMovie(id:Int):Single<List<Movie>>{
        return movieDao!!.checkMovie(id)
    }

    fun getAllMovie():Single<List<Movie>>{
        return movieDao!!.allMovie()
    }
}