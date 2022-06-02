package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Description
import com.example.nasa.domain.repository.RemoteRepository

class GetDescriptionUseCase(
    private val remoteRepository: RemoteRepository
) {
    suspend operator fun invoke(nasaId: String): Result<Description> =
        remoteRepository.fetchDescription(nasaId)
}