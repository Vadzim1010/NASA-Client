package com.example.nasa.repository

import com.example.nasa.model.NasaImage

interface RemoteNasaImagesRepository {

    suspend fun fetchNasaImages(page: Int): Result<List<NasaImage>>

    suspend fun getContentLength() : Int
}