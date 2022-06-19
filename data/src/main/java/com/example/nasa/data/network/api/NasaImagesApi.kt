package com.example.nasa.data.network.api

import com.example.nasa.data.model.DescriptionDto
import com.example.nasa.data.model.NasaImageDto
import com.example.nasa.domain.util.MEDIA_TYPE
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
    ): NasaImageDto

    @GET("/search")
    suspend fun getDescription(
        @Query("nasa_id") nasaId: String,
    ): DescriptionDto
}