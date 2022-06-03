package com.example.nasa.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nasa.data.database.dao.ApodDao
import com.example.nasa.data.database.dao.DescriptionDao
import com.example.nasa.data.database.dao.ImageDescriptionDao
import com.example.nasa.data.database.dao.NasaImageDao
import com.example.nasa.data.database.entity.ApodEntity
import com.example.nasa.data.database.entity.DescriptionEntity
import com.example.nasa.data.database.entity.NasaImageEntity

@Database(
    entities = [NasaImageEntity::class, DescriptionEntity::class, ApodEntity::class],
    version = 1
)
internal abstract class NasaDatabase : RoomDatabase() {

    abstract fun getImageDao(): NasaImageDao

    abstract fun getDescriptionDao(): DescriptionDao

    abstract fun getImageDescriptionDao(): ImageDescriptionDao

    abstract fun getApodDao(): ApodDao
}