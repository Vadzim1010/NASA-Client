package com.example.nasa.repository

import com.example.nasa.model.NasaImage

interface LocalNasaImagesRepository {

    suspend fun getImagePage(page: Int): List<NasaImage>

    suspend fun insertImagePage(page: Int, pageList: List<NasaImage>)
}