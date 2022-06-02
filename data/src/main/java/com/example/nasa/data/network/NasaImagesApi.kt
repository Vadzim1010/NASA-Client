package com.example.nasa.data.network

import com.example.nasa.data.util.MEDIA_TYPE
import retrofit2.http.GET
import retrofit2.http.Query


internal interface NasaImagesApi {

    @GET("/search")
    suspend fun getNasaImages(
        @Query("page") page: Int,
        @Query("q") searchQuery: String,
        @Query("year_start") yearStart: Int,
        @Query("year_end") yearEnd: Int,
        @Query("media_type") mediaType: String = MEDIA_TYPE,
    ): NasaResponse

    @GET("/search")
    suspend fun getDescription(
        @Query("nasa_id") nasaId: String,
    ): NasaResponse
}