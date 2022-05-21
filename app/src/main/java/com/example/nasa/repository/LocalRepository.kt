package com.example.nasa.repository

import com.example.nasa.database.dao.NasaImageDao
import com.example.nasa.model.NasaImage
import com.example.nasa.utils.mapToEntity
import com.example.nasa.utils.mapToModel

class LocalRepository(private val nasaImageDao: NasaImageDao) {


    suspend fun getAll() = nasaImageDao
        .getAll()
        .mapToModel

    suspend fun getImagePage(page: Int) = nasaImageDao
        .getImagesPage(page)
        .mapToModel

    suspend fun insertImagePage(page: Int, pageList: List<NasaImage>) {
        nasaImageDao.insertImagePage(pageList.mapToEntity(page))
    }
}