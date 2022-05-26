package com.example.nasa.repository

import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams

interface LocalRepository {

    suspend fun getImagePage(page: Int, searchParams: SearchParams): List<NasaImage>

    suspend fun insertImagePage(page: Int, searchParams: SearchParams, pageList: List<NasaImage>)
}