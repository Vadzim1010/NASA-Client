package com.example.nasa.data.network.api

import com.example.nasa.data.network.CountriesResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET("all")
    suspend fun getCountries(): List<CountriesResponseItem>

    @GET("name/{name}")
    suspend fun getCountryByName(
        @Path("name") name: String
    ): List<CountriesResponseItem>
}