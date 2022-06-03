package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Apod
import kotlinx.coroutines.flow.Flow

interface LocalApodRepository {

    fun getPictureOfDay(): Flow<List<Apod>>

    suspend fun insertPictureOfDay(apod: Apod)

    suspend fun deletePictureOfDay()
}