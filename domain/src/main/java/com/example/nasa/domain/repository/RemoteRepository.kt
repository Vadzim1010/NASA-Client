package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Apod

interface RemoteRepository {


    suspend fun fetchNasaImages(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): Result<List<NasaImage>>

    suspend fun fetchDescription(nasaId: String): Result<Description>

    suspend fun fetchPictureOfDay(): Result<Apod>
}