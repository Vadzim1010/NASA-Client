package com.example.nasa.data.model

import com.example.nasa.domain.model.Apod

internal data class ApodDto(
    val title: String,
    val date: String,
    val imageUrl: String,
) {
    fun toDomain() = Apod(
        title = title,
        date = date,
        imageUrl = imageUrl
    )
}
