package com.example.nasa.repository.impl

import com.example.nasa.model.SearchParams
import com.example.nasa.network.NasaApi
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.mapToModel
import kotlinx.coroutines.delay

class RemoteRepositoryImpl(
    private val nasaApi: NasaApi,
) : RemoteRepository {

    private var contentLength = 0

    override suspend fun fetchNasaImages(page: Int, searchParams: SearchParams) = runCatching {
        delay(2000) // delay for testing
        val response = nasaApi.getNasaImages(
            page = page,
            searchParams = searchParams.search,
            yearStart = searchParams.yearStart,
            yearEnd = searchParams.yearEnd
        )

        contentLength = response.collection.metadata.totalHits

        response
            .mapToModel
    }

    override suspend fun getContentLength() = contentLength
}