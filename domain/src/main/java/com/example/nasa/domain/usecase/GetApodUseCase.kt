package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.repository.RemoteApodRepository

class GetApodUseCase(private val remoteRepository: RemoteApodRepository) {

    suspend operator fun invoke(): Result<Apod> =
        remoteRepository.fetchPictureOfDay()
}