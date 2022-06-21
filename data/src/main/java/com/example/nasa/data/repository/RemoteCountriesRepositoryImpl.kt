package com.example.nasa.data.repository

import com.example.nasa.data.network.api.CountriesApi
import com.example.nasa.domain.model.Country
import com.example.nasa.domain.repository.RemoteCountriesRepository

class RemoteCountriesRepositoryImpl(
    private val countriesApi: CountriesApi
) : RemoteCountriesRepository {

    override suspend fun getCountries() =
        runCatching {
            countriesApi.getCountries()
                .map { item ->
                    Country(
                        name = item.name.common,
                        lat = item.latlng.getOrNull(0) ?: 0.0,
                        lng = item.latlng.getOrNull(1) ?: 0.0,
                        flagImage = item.flags.png,
                    )
                }
        }
}