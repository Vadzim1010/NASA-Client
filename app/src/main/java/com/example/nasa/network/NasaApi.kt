package com.example.nasa.network

import com.example.nasa.network.model.NasaResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaApi {

    @GET("/search")
    suspend fun getNasaImages(
        @Query("page") page: Int,
        @Query("year_start") yearStart: Int,
        @Query("media_type") mediaType: String = "image",
    ): NasaResponse
}