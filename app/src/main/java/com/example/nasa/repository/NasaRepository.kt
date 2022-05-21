package com.example.nasa.repository

import com.example.nasa.model.NasaImage
import com.example.nasa.retrofit.NasaApi

class NasaRepository(private val nasaApi: NasaApi) {

    suspend fun fetchNasaImages(page: Int) = runCatching {
        nasaApi.getNasaImages(page, 2015)
            .collection
            .items
            .map { item ->
                NasaImage(
                    id = item.data.get(0).nasaId,
                    imageUrl = item.links.get(0).href
                )
            }
    }
}