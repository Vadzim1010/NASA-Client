package com.example.nasa.data.network.api

import com.example.nasa.data.network.NasaApodResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface NasaApi {

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String,
    ): NasaApodResponse
}