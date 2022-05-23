package com.example.nasa.repository.impl

import com.example.nasa.network.NasaApi
import com.example.nasa.repository.RemoteNasaImagesRepository
import com.example.nasa.utils.mapToModel
import kotlinx.coroutines.delay

class RemoteNasaImagesRepositoryImpl(
    private val nasaApi: NasaApi,
) : RemoteNasaImagesRepository {

    private var contentLength = 0

    override suspend fun fetchNasaImages(page: Int) = runCatching {
        delay(2000) // delay for testing
        val response = nasaApi.getNasaImages(page, 2015)

        contentLength = response.collection.metadata.totalHits

        response
            .mapToModel
    }

    override suspend fun getContentLength() = contentLength
}