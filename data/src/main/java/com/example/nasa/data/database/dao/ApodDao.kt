package com.example.nasa.data.database.dao

import androidx.room.*
import com.example.nasa.data.database.entity.ApodEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ApodDao {

    @Query("SELECT * FROM ApodEntity LIMIT 1")
    fun getPictureOfDay(): Flow<List<ApodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(apod: ApodEntity)

    @Query("DELETE FROM ApodEntity")
    suspend fun deletePictureOfDay()
}