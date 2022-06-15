package com.example.nasa.data.repository

import com.example.nasa.data.database.dao.NasaImageDao
import com.example.nasa.data.util.mapToDomain
import com.example.nasa.data.util.mapToEntity
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.repository.LocalImagesRepository
import com.example.nasa.domain.util.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

internal class LocalImagesRepositoryImpl(
    private val nasaImageDao: NasaImageDao,
) : LocalImagesRepository {


    override fun getImagePage(
        page: Int,
        query: String,
        startYear: Int,
        endYear: Int
    ): Flow<List<NasaImage>> = nasaImageDao
        .getImagesPage(
            limit = (page * PAGE_SIZE),
            search = query,
            yearStart = startYear,
            yearEnd = endYear
        )
        .mapToDomain()

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