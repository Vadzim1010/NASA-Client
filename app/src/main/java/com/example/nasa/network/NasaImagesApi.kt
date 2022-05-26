package com.example.nasa.network

import com.example.nasa.network.model.NasaResponse
import com.example.nasa.utils.MEDIA_TYPE
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaImagesApi {

    @GET("/search")
    suspend fun getNasaImages(
        @Query("page") page: Int,
        @Query("q") searchParams: String,
        @Query("year_start") yearStart: Int,
        @Query("year_end") yearEnd: Int,
        @Query("media_type") mediaType: String = MEDIA_TYPE,
    ): NasaResponse

    @GET("/search")
    suspend fun getDescription(
        @Query("nasa_id") nasaId: String,
    ): NasaResponse
}