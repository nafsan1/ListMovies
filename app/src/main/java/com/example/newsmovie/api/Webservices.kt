package com.example.newsmovie.api

import com.example.newsmovie.model.ResponseMovie
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Webservices {
    @GET("movie/{category}")
     fun getMovie(
        @Path("category") category: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Deferred<ResponseMovie>
}

val myApi: Webservices by lazy {

 /*   val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()*/

    Retrofit.Builder()

        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Webservices::class.java)
}