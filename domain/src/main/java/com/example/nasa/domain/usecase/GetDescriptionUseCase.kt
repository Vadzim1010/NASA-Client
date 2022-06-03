package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.LocalDescRepository
import com.example.nasa.domain.repository.RemoteDescRepository
import com.example.nasa.domain.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class GetDescriptionUseCase(
    private val remoteRepository: RemoteDescRepository,
    private val localDescRepository: LocalDescRepository
) {


    operator fun invoke(nasaId: String): Flow<Resource<List<Description>>> =
        networkBoundResource(
            query = {
                localDescRepository.getDescription(id = nasaId)
            },
            fetch = {
                remoteRepository.fetchDescription(nasaId = nasaId)
            },
            saveFetchResult = {
                localDescRepository.insertDescription(it)
            }
        )
}