package com.example.nasa.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.nasa.data.database.entity.DescriptionEntity

@Dao
interface DescriptionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDescription(description: DescriptionEntity)
}