package com.example.nasa.data.util

import android.util.Log
import com.example.nasa.data.database.entity.DescriptionEntity
import com.example.nasa.data.database.entity.ImageDescriptionEntity
import com.example.nasa.data.database.entity.NasaImageEntity
import com.example.nasa.data.network.model.*
import com.example.nasa.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

internal fun Flow<List<NasaImageEntity>>.mapToDomain(): Flow<List<NasaImage>> =
    this.map { list -> list.map { it.toDomain() } }

internal fun Flow<List<ImageDescriptionEntity>>.mapDescToDomain(): Flow<List<Description>> =
    this.map { list -> list.map { it.toDomain() } }

internal fun List<CountryDto>.mapToDomain(): List<Country> =
    this.map { item ->
        Country(
            name = item.name.common,
            lat = item.latlng.getOrNull(0) ?: 0.0,
            lng = item.latlng.getOrNull(1) ?: 0.0,
        )
    }

internal fun NasaImageDto.mapToDomain(): List<NasaImage> = this
    .collection
    .items
    .map { item ->
        NasaImage(
            id = item.data.getOrNull(0)?.nasaId ?: UUID.randomUUID().toString(),
            imageUrl = item.links.getOrNull(0)?.href ?: "",
            totalHits = this.collection.metadata.totalHits
        )
    }

internal fun DescriptionDto.mapToDomain(): List<Description> = this
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

internal fun Description.toEntity(): DescriptionEntity = DescriptionEntity(
    nasaImageId = this.nasaId,
    title = this.title,
    description = this.description,
    imageUrl = this.imageUrl
)

internal fun ImageDescriptionEntity.toDomain() = Description(
    nasaId = descriptionEntity?.nasaImageId ?: "",
    title = descriptionEntity?.title ?: "",
    description = descriptionEntity?.description ?: "",
    imageUrl = nasaImageEntity.imageUrl
)

internal fun NasaImageEntity.toDomain() = NasaImage(
    id = id,
    imageUrl = imageUrl,
    totalHits = totalHits,
)

internal fun CountryFlagDto.toDomain() =
    CountryFlag(
        countryName = this.name.common,
        flagImageUrl = this.flags.png,
    )

internal fun emptyCountryFlag() =
    CountryFlag("", "")

fun log(message: String) {
    Log.i("ProjectNasa", message)
}
