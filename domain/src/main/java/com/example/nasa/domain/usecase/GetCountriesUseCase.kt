package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.Country
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.RemoteCountriesRepository
import kotlinx.coroutines.flow.flow

class GetCountriesUseCase(private val remoteRepository: RemoteCountriesRepository) {

    suspend operator fun invoke() = flow<Resource<List<Country>>> {
        remoteRepository.getCountries()
            .fold(
                onSuccess = { emit(Resource.Success(it)) },
                onFailure = { emit(Resource.Error(throwable = it)) }
            )
    }
}