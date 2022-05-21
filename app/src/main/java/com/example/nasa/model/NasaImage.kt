package com.example.nasa.model

import com.example.nasa.database.entity.NasaImageEntity

data class NasaImage(
    val id: String,
    val imageUrl: String,
) {

    fun toEntity(page: Int) = NasaImageEntity(
        id = id,
        imageUrl = imageUrl,
        page = page,
    )
}
