package com.example.nasa.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ImageDescriptionEntity(
    @Embedded
    val nasaImageEntity: NasaImageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "nasa_image_id"
    )
    val descriptionEntity: DescriptionEntity?
)
