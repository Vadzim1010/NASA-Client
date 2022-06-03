package com.example.nasa.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.nasa.data.database.entity.ImageDescriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDescriptionDao {

    @Transaction
    @Query("SELECT * FROM NasaImageEntity WHERE id LIKE (:imageId) LIMIT 1")
    fun getImageDescription(imageId: String): Flow<List<ImageDescriptionEntity>>
}