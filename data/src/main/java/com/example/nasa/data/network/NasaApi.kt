package com.example.nasa.data.network

import retrofit2.http.GET
import retrofit2.http.Query

internal interface NasaApi {

    @GET("/planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String,
    ): NasaApodResponse
}