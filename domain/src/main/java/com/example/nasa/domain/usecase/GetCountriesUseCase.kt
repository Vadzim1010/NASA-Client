package com.example.nasa.domain.usecase

import com.example.nasa.domain.repository.RemoteCountriesRepository

class GetCountriesUseCase(private val remoteRepository: RemoteCountriesRepository) {

    suspend operator fun invoke() = remoteRepository.getCountries()
}