package com.example.nasa.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nasa.data.database.entity.NasaImageEntity

@Dao
internal interface NasaImageDao {


    @Query(
        "SELECT * FROM NasaImageEntity " +
                "WHERE (:page) = page " +
                "AND (:search) = search " +
                "AND (:yearStart) = year_start " +
                "AND (:yearEnd) = year_end;"
    )
    suspend fun getImagesPage(
        page: Int,
        search: String,
        yearStart: Int,
        yearEnd: Int
    ): List<NasaImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagePage(imagePage: List<NasaImageEntity>)
}