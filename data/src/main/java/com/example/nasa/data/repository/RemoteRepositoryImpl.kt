package com.example.nasa.data.repository


import com.example.nasa.data.network.ApiConfig
import com.example.nasa.data.network.NasaApi
import com.example.nasa.data.network.NasaImagesApi
import com.example.nasa.data.util.mapToModel
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.repository.RemoteRepository
import kotlinx.coroutines.delay


internal class RemoteRepositoryImpl(
    private val nasaImagesApi: NasaImagesApi,
    private val nasaApi: NasaApi,
) : RemoteRepository {


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
        ).mapToModel
    }

    override suspend fun fetchDescription(nasaId: String) = runCatching {
        delay(2000) // delay for testing
        nasaImagesApi.getDescription(nasaId)
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

    override suspend fun fetchPictureOfDay() = runCatching {
        delay(2000) //delay for testing
        val response = nasaApi.getPictureOfDay(ApiConfig.API_KEY)
        Apod(
            title = response.title,
            date = response.date,
            imageUrl = response.url
        )
    }
}