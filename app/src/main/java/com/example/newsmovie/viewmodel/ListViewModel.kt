package com.example.newsmovie.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsmovie.R
import com.example.newsmovie.api.room.AppDatabase
import com.example.newsmovie.model.Movie
import com.example.newsmovie.repository.MovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ListViewModel(
    application:
    Application
) : AndroidViewModel(application) {

    private var repository: MovieRepository
    val resultMovie = MutableLiveData<List<Movie>>()
    val error = MutableLiveData<String>()
    val succes = MutableLiveData<String>()
    private val disposable = CompositeDisposable()

    init {
        val movieDao = AppDatabase.getInstance(application).moviesDao()
        repository = MovieRepository(movieDao)

    }

    fun getMovie(category: String) {

        Log.e("MoviesAdapter", "OnClick")

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    val result = repository.getMovie(
                        category,
                        getApplication<Application>().resources.getString(
                            R.string.api_key
                        )
                    )
                    val response = result.await()

                    resultMovie.postValue(response.movies)
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> {
                            error.postValue("NetWork Error")
                        }
                        is HttpException -> {
                            val code = throwable.code()
                            val errorResponse = throwable.message()
                            error.postValue("Error $code $errorResponse")
                        }
                        else -> {
                            error.postValue(throwable.message)
                            Log.e("ListView", throwable.message.toString())
                        }
                    }
                }
            }
        }
    }

    fun insertMovie(movie: Movie){
        disposable.add(
            repository.insert(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        succes.postValue("Add Movie")
                    }

                    override fun onError(e: Throwable) {
                        error.postValue(e.message)


                    }
                })
        )

    }

    fun deleteMovie(movie: Movie){
        disposable.add(
            repository.delete(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        succes.postValue("Remove Movie")
                    }

                    override fun onError(e: Throwable) {
                        error.postValue(e.message)


                    }
                })
        )

    }

    fun getCheckMovie(id: Int): LiveData<Boolean> {
        var check = MutableLiveData<Boolean>()

        disposable.add(
            repository.getCheckMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie?>>() {
                    override fun onSuccess(t: List<Movie?>) {
                     if (t.size > 0){
                         check.postValue(true)
                     }else{
                         check.postValue(false)
                     }
                    }

                    override fun onError(e: Throwable) {
                        error.postValue(e.message)
                        Log.e("ListView", e.message.toString())

                    }
                })
        )
         return check
    }

    fun getFavoriteMovie() {
        disposable.add(
            repository.getAllMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {
                    override fun onSuccess(t: List<Movie>) {
                        resultMovie.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        error.postValue(e.message)
                        Log.e("ListView", e.message.toString())

                    }
                })
        )
    }


}