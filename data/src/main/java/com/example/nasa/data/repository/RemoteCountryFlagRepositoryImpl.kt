package com.example.nasa.data.repository

import com.example.nasa.data.network.api.CountriesApi
import com.example.nasa.data.util.emptyCountryFlag
import com.example.nasa.data.util.toDomain
import com.example.nasa.domain.model.CountryFlag
import com.example.nasa.domain.repository.RemoteCountryFlagRepository
import kotlinx.coroutines.delay

class RemoteCountryFlagRepositoryImpl(
    private val countriesApi: CountriesApi,
) : RemoteCountryFlagRepository {


    override suspend fun getCountryByName(name: String): Result<CountryFlag> =
        runCatching {
            delay(2000) //delay for testing
            countriesApi.getCountryByName(name)
                .getOrNull(0)
                ?.toDomain() ?: emptyCountryFlag()
        }
}