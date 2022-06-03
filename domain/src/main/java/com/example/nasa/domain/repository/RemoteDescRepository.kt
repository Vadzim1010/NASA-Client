package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Description

interface RemoteDescRepository {


    suspend fun fetchDescription(nasaId: String): Result<Description>
}