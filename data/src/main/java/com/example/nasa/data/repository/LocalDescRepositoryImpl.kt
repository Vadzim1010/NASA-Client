package com.example.nasa.data.repository

import com.example.nasa.data.database.dao.DescriptionDao
import com.example.nasa.data.database.dao.ImageDescriptionDao
import com.example.nasa.data.util.mapDescToDomain
import com.example.nasa.data.util.toEntity
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.repository.LocalDescRepository
import kotlinx.coroutines.flow.Flow

class LocalDescRepositoryImpl(
    private val descriptionDao: DescriptionDao,
    private val imageDescriptionDao: ImageDescriptionDao,
) : LocalDescRepository {


    override fun getDescription(id: String): Flow<List<Description>> =
        imageDescriptionDao.getImageDescription(id)
            .mapDescToDomain()


    override suspend fun insertDescription(description: Description) {
        descriptionDao.insertDescription(description.toEntity())
    }
}