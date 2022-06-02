package com.example.nasa.domain.repository

import com.example.nasa.domain.model.NasaImage


interface LocalRepository {


    suspend fun getImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int,
    ): List<NasaImage>

    suspend fun insertImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int,
        pageList: List<NasaImage>,
    )
}