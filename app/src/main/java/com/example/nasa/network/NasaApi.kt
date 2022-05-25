package com.example.nasa.network

import com.example.nasa.network.model.NasaResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaApi {

    @GET("/search")
    suspend fun getNasaImages(
        @Query("page") page: Int,
        @Query("q") searchParams: String,
        @Query("year_start") yearStart: Int,
        @Query("year_end") yearEnd: Int,
        @Query("media_type") mediaType: String = "image",
    ): NasaResponse

    @GET("/search")
    suspend fun getDescription(
        @Query("nasa_id") nasaId: String
    ): NasaResponse
}