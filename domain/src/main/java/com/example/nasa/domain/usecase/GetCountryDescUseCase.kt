package com.example.nasa.domain.usecase

import com.example.nasa.domain.model.CountryFlag
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.RemoteCountryFlagRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class GetCountryDescUseCase(private val remoteRepository: RemoteCountryFlagRepository) {

    suspend operator fun invoke(name: String) = flow<Resource<CountryFlag>> {
        remoteRepository.getCountryByName(name)
            .fold(
                onSuccess = { emit(Resource.Success(it)) },
                onFailure = { emit(Resource.Error(throwable = it)) }
            )

    }
        .onStart {
            emit(Resource.Loading())
        }
}