package com.example.nasa.repository.impl

import com.example.nasa.model.Description
import com.example.nasa.model.SearchParams
import com.example.nasa.network.NasaApi
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.mapToModel
import kotlinx.coroutines.delay

class RemoteRepositoryImpl(
    private val nasaApi: NasaApi,
) : RemoteRepository {


    override suspend fun fetchNasaImages(page: Int, searchParams: SearchParams) = runCatching {
        delay(2000) // delay for testing
        nasaApi.getNasaImages(
            page = page,
            searchParams = searchParams.search,
            yearStart = searchParams.yearStart,
            yearEnd = searchParams.yearEnd
        ).mapToModel
    }

    override suspend fun fetchDescription(nasaId: String) = runCatching {
        delay(2000) // delay for testing
        nasaApi.getDescription(nasaId)
            .collection
            .items
            .map { item ->
                Description(
                    nasaId = item.data.getOrNull(0)?.nasaId ?: "",
                    title = item.data.getOrNull(0)?.title ?: "",
                    description = item.data.getOrNull(0)?.description ?: "",
                    imageUrl = item.links.getOrNull(0)?.href ?: "",
                )
            }.first { it.nasaId.isNotBlank() }
    }
}