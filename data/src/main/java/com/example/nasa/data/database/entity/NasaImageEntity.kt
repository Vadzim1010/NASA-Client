package com.example.nasa.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NasaImageEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "page")
    val page: Int,
    @ColumnInfo(name = "search")
    val search: String,
    @ColumnInfo(name = "year_start")
    val yearStart: Int,
    @ColumnInfo(name = "year_end")
    val yearEnd: Int,
    @ColumnInfo(name = "total_hits")
    val totalHits: Int
)