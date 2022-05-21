package com.example.nasa.repository

import com.example.nasa.network.NasaApi
import com.example.nasa.utils.log
import com.example.nasa.utils.mapToModel

class RemoteRepository(private val nasaApi: NasaApi) {


    suspend fun fetchNasaImages(page: Int) = runCatching {
        nasaApi.getNasaImages(page, 2015)
            .mapToModel
    }
}