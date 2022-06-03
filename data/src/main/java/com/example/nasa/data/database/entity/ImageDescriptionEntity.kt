package com.example.nasa.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.example.nasa.domain.model.Description

data class ImageDescriptionEntity(
    @Embedded
    val nasaImageEntity: NasaImageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "nasa_image_id"
    )
    val descriptionEntity: DescriptionEntity?
) {
    fun toDomain() = Description(
        nasaId = descriptionEntity?.nasaImageId ?: "",
        title = descriptionEntity?.title ?: "",
        description = descriptionEntity?.description ?: "",
        imageUrl = nasaImageEntity.imageUrl
    )
}
