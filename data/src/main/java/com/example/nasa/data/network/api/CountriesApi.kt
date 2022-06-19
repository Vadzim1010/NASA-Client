package com.example.nasa.data.network.api

import com.example.nasa.data.network.CountriesResponseItem
import retrofit2.http.GET

interface CountriesApi {

    @GET("all")
    suspend fun getCountries(): List<CountriesResponseItem>
}