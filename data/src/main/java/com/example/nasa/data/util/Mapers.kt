package com.example.nasa.data.util

import android.util.Log
import com.example.nasa.data.database.entity.ApodEntity
import com.example.nasa.data.database.entity.DescriptionEntity
import com.example.nasa.data.database.entity.ImageDescriptionEntity
import com.example.nasa.data.database.entity.NasaImageEntity
import com.example.nasa.data.model.ApodDto
import com.example.nasa.data.model.DescriptionDto
import com.example.nasa.data.model.NasaImageDto
import com.example.nasa.data.network.NasaApodResponse
import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.NasaImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*


internal val Flow<List<NasaImageEntity>>.mapToDomain: Flow<List<NasaImage>>
    get() = this.map { list -> list.map { it.toDomain() } }


internal val Flow<List<ImageDescriptionEntity>>.mapDescToDomain: Flow<List<Description>>
    get() = this.map { list -> list.map { it.toDomain() } }

internal val Flow<List<ApodEntity>>.mapApodToDomain: Flow<List<Apod>>
    get() = this.map { list -> list.map { it.toDomain() } }


internal val NasaImageDto.mapToDomain: List<NasaImage>
    get() = this
        .collection
        .items
        .map { item ->
            NasaImage(
                id = item.data.getOrNull(0)?.nasaId ?: UUID.randomUUID().toString(),
                imageUrl = item.links.getOrNull(0)?.href ?: "",
                totalHits = this.collection.metadata.totalHits
            )
        }


internal val DescriptionDto.mapToDomain: List<Description>
    get() = this
        .collection
        .items
        .map { item ->
            Description(
                nasaId = item.data.getOrNull(0)?.nasaId ?: "",
                title = item.data.getOrNull(0)?.title ?: "",
                description = item.data.getOrNull(0)?.description ?: "",
                imageUrl = item.links.getOrNull(0)?.href ?: "",
            )
        }


internal val NasaApodResponse.toDomain: Apod
    get() = ApodDto(
        title = this.title,
        date = this.date,
        imageUrl = this.url
    ).toDomain()


internal val Description.toEntity: DescriptionEntity
    get() = DescriptionEntity(
        nasaImageId = this.nasaId,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl
    )

internal val Apod.toEntity: ApodEntity
    get() = ApodEntity(
        title = this.title,
        date = this.date,
        imageUrl = this.imageUrl,
    )


internal fun List<NasaImage>.mapToEntity(
    page: Int,
    searchQuery: String,
    startYear: Int,
    endYear: Int,

) = this.map { nasaImage ->
    NasaImageEntity(
        id = nasaImage.id,
        imageUrl = nasaImage.imageUrl,
        page = page,
        search = searchQuery,
        yearStart = startYear,
        yearEnd = endYear,
        totalHits = nasaImage.totalHits,
    )
}


fun log(message: String) {
    Log.i("ProjectNasa", message)
}
