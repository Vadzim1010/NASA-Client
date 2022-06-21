package com.example.nasa.domain.repository

import com.example.nasa.domain.model.CountryFlag

interface RemoteCountryFlagRepository {

    suspend fun getCountryByName(name: String): Result<CountryFlag>
}