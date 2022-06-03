package com.example.nasa.domain.repository

import com.example.nasa.domain.model.NasaImage
import kotlinx.coroutines.flow.Flow


interface LocalImagesRepository {


    fun getImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int,
    ): Flow<List<NasaImage>>

    suspend fun insertImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int,
        pageList: List<NasaImage>,
    )
}