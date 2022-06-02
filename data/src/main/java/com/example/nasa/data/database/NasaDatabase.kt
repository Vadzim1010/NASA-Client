package com.example.nasa.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nasa.data.database.dao.NasaImageDao
import com.example.nasa.data.database.entity.NasaImageEntity

@Database(entities = [NasaImageEntity::class], version = 1)
internal abstract class NasaDatabase : RoomDatabase() {

    abstract fun getImageDao(): NasaImageDao
}