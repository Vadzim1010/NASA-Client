package com.example.nasa.network

import com.example.nasa.network.model.NasaApodResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String,
    ): NasaApodResponse
}