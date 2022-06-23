package com.example.nasa.data.repository

import com.example.nasa.data.network.api.CountriesApi
import com.example.nasa.data.util.mapToDomain
import com.example.nasa.domain.model.Country
import com.example.nasa.domain.repository.RemoteCountriesRepository

class RemoteCountriesRepositoryImpl(
    private val countriesApi: CountriesApi
) : RemoteCountriesRepository {

    override suspend fun getCountries(): Result<List<Country>> =
        runCatching {
            countriesApi.getCountries()
                .mapToDomain()
        }
}