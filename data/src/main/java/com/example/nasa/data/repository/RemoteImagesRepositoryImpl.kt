package com.example.nasa.data.repository


import com.example.nasa.data.network.NasaImagesApi
import com.example.nasa.data.util.mapToDomain
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.repository.RemoteImagesRepository
import kotlinx.coroutines.delay


internal class RemoteImagesRepositoryImpl(
    private val nasaImagesApi: NasaImagesApi,
) : RemoteImagesRepository {


    override suspend fun fetchNasaImages(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): Result<List<NasaImage>> = runCatching {
        delay(2000) // delay for testing
        nasaImagesApi.getNasaImages(
            page = page,
            searchQuery = query,
            yearStart = startYear,
            yearEnd = endYear,
        ).mapToDomain
    }
}