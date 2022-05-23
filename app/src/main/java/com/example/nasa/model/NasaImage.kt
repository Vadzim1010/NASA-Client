package com.example.nasa.model

import com.example.nasa.database.entity.NasaImageEntity

data class NasaImage(
    val id: String,
    val imageUrl: String,
) {

    fun toEntity(page: Int, searchParams: SearchParams) = NasaImageEntity(
        id = id,
        imageUrl = imageUrl,
        page = page,
        search = searchParams.search,
        yearStart = searchParams.yearStart,
        yearEnd = searchParams.yearEnd,
    )
}
