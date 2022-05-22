package com.example.nasa.repository.impl

import com.example.nasa.database.dao.NasaImageDao
import com.example.nasa.model.NasaImage
import com.example.nasa.repository.LocalNasaImagesRepository
import com.example.nasa.utils.mapToEntity
import com.example.nasa.utils.mapToModel

class LocalNasaImagesRepositoryImpl(
    private val nasaImageDao: NasaImageDao,
) : LocalNasaImagesRepository {


    override suspend fun getImagePage(page: Int) = nasaImageDao
        .getImagesPage(page)
        .mapToModel

    override suspend fun insertImagePage(page: Int, pageList: List<NasaImage>) {
        nasaImageDao.insertImagePage(pageList.mapToEntity(page))
    }
}