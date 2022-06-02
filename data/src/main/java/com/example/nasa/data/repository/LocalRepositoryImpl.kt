package com.example.nasa.data.repository

import com.example.nasa.data.database.dao.NasaImageDao
import com.example.nasa.data.util.mapToEntity
import com.example.nasa.data.util.mapToModel
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.repository.LocalRepository

internal class LocalRepositoryImpl(
    private val nasaImageDao: NasaImageDao,
) : LocalRepository {


    override suspend fun getImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): List<NasaImage> = nasaImageDao
        .getImagesPage(
            page = page,
            search = query,
            yearStart = startYear,
            yearEnd = endYear,
        )
        .mapToModel

    override suspend fun insertImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int,
        pageList: List<NasaImage>
    ) {
        nasaImageDao.insertImagePage(pageList.mapToEntity(page, query, startYear, endYear))
    }
}