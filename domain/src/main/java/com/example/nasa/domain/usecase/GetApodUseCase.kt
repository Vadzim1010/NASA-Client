package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.repository.RemoteRepository

class GetApodUseCase(private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(): Result<Apod> =
        remoteRepository.fetchPictureOfDay()
}