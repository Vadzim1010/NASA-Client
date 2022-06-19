package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Country

interface RemoteCountriesRepository {

    suspend fun getCountries(): Result<List<Country>>
}