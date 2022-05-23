package com.example.nasa.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nasa.model.NasaImage

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
) {

    fun toModel() = NasaImage(
        id = id,
        imageUrl = imageUrl,
    )
}
