package com.example.nasa.repository

import com.example.nasa.model.Description
import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams

interface RemoteRepository {

    suspend fun fetchNasaImages(page: Int, searchParams: SearchParams): Result<List<NasaImage>>

    suspend fun fetchDescription(nasaId: String): Result<Description>
}