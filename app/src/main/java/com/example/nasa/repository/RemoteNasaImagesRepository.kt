package com.example.nasa.repository

import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams

interface RemoteNasaImagesRepository {

    suspend fun fetchNasaImages(page: Int, searchParams: SearchParams): Result<List<NasaImage>>

    suspend fun getContentLength(): Int
}