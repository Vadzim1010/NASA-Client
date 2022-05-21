package com.example.nasa.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nasa.database.dao.NasaImageDao
import com.example.nasa.database.entity.NasaImageEntity

@Database(entities = [NasaImageEntity::class], version = 1)
abstract class NasaDatabase : RoomDatabase() {

    abstract fun getImageDao(): NasaImageDao
}