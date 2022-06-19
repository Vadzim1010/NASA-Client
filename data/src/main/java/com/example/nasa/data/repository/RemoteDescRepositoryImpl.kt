package com.example.nasa.data.repository

import com.example.nasa.data.network.api.NasaImagesApi
import com.example.nasa.data.util.mapToDomain
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.repository.RemoteDescRepository
import kotlinx.coroutines.delay

internal class RemoteDescRepositoryImpl(
    private val nasaImagesApi: NasaImagesApi
) : RemoteDescRepository {


    override suspend fun fetchDescription(nasaId: String): Result<Description> = runCatching {
        delay(2000) // delay for testing
        nasaImagesApi.getDescription(nasaId)
            .mapToDomain()
            .first { it.nasaId.isNotBlank() }
    }
}