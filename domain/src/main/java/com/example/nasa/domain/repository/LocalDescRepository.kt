package com.example.nasa.domain.repository

import com.example.nasa.domain.model.Description
import kotlinx.coroutines.flow.Flow

interface LocalDescRepository {


    fun getDescription(id: String): Flow<List<Description>>


    suspend fun insertDescription(description: Description)
}