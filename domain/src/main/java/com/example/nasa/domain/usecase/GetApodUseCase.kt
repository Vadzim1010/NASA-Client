package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.LocalApodRepository
import com.example.nasa.domain.repository.RemoteApodRepository
import com.example.nasa.domain.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class GetApodUseCase(
    private val remoteRepository: RemoteApodRepository,
    private val localApodRepository: LocalApodRepository
) {

    operator fun invoke(): Flow<Resource<List<Apod>>> =
        networkBoundResource(
            query = {
                localApodRepository.getPictureOfDay()
            },
            fetch = {
                remoteRepository.fetchPictureOfDay()
            },
            saveFetchResult = { apod ->
                localApodRepository.deletePictureOfDay()
                localApodRepository.insertPictureOfDay(apod)
            }
        )
}