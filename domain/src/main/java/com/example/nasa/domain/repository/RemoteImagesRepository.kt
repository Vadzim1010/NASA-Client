package com.example.nasa.domain.repository

import com.example.nasa.domain.model.NasaImage

interface RemoteImagesRepository {

    suspend fun fetchNasaImages(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): Result<List<NasaImage>>
}
