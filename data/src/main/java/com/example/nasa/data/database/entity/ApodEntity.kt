package com.example.nasa.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nasa.domain.model.Apod

@Entity
data class ApodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
) {
    fun toDomain() = Apod(
        title = title,
        date = date,
        imageUrl = imageUrl,
    )
}