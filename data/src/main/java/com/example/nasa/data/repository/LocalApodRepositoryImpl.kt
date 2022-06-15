package com.example.nasa.data.repository

import com.example.nasa.data.database.dao.ApodDao
import com.example.nasa.data.util.mapApodToDomain
import com.example.nasa.data.util.toEntity
import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.repository.LocalApodRepository
import kotlinx.coroutines.flow.Flow


internal class LocalApodRepositoryImpl(private val apodDao: ApodDao) : LocalApodRepository {


    override fun getPictureOfDay(): Flow<List<Apod>> =
        apodDao.getPictureOfDay()
            .mapApodToDomain()


    override suspend fun insertPictureOfDay(apod: Apod) {
        apodDao.insertPictureOfDay(apod.toEntity())
    }


    override suspend fun deletePictureOfDay() {
        apodDao.deletePictureOfDay()
    }
}