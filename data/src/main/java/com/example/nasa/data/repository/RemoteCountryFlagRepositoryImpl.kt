package com.example.nasa.data.repository

import com.example.nasa.data.network.api.CountriesApi
import com.example.nasa.domain.model.CountryFlag
import com.example.nasa.domain.repository.RemoteCountryFlagRepository

class RemoteCountryFlagRepositoryImpl(
    private val countriesApi: CountriesApi,
) : RemoteCountryFlagRepository {


    override suspend fun getCountryByName(name: String): Result<CountryFlag> =
        runCatching {
            val response = countriesApi.getCountryByName(name)

            CountryFlag(
                countryName = response.getOrNull(0)?.name?.common ?: "",
                flagImageUrl = response.getOrNull(0)?.flags?.png ?: "",
            )
        }
}