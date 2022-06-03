package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Apod

interface RemoteApodRepository {


    suspend fun fetchPictureOfDay(): Result<Apod>
}