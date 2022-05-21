package com.example.nasa.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nasa.database.entity.NasaImageEntity

@Dao
interface NasaImageDao {

    @Query("SELECT * FROM NasaImageEntity ORDER BY page ASC")
    suspend fun getAll(): List<NasaImageEntity>

    @Query("SELECT * FROM NasaImageEntity WHERE (:page) LIKE page")
    suspend fun getImagesPage(page: Int): List<NasaImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagePage(imagePage: List<NasaImageEntity>)
}