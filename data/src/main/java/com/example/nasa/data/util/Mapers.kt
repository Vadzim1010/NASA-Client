package com.example.nasa.data.util

import android.util.Log
import com.example.nasa.data.database.entity.NasaImageEntity
import com.example.nasa.data.model.SearchParams
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.data.network.NasaResponse
import java.util.*


internal val List<NasaImageEntity>.mapToModel: List<NasaImage>
    get() = this.map { it.toModel() }

internal val NasaResponse.mapToModel: List<NasaImage>
    get() = this
        .collection
        .items.map { item ->
            NasaImage(
                id = item.data.getOrNull(0)?.nasaId ?: UUID.randomUUID().toString(),
                imageUrl = item.links.getOrNull(0)?.href ?: "",
            )
        }


internal fun List<NasaImage>.mapToEntity(
    page: Int,
    searchQuery: String,
    startYear: Int,
    endYear: Int
) =
    this.map { nasaImage ->
        NasaImageEntity(
            id = nasaImage.id,
            imageUrl = nasaImage.imageUrl,
            page = page,
            search = searchQuery,
            yearStart = startYear,
            yearEnd = endYear,
        )
    }

fun log(message: String) {
    Log.i("TAG", message)
}


internal val SearchParams.checkFormat: Boolean
    get() = this.yearStart in MIN_YEAR until MAX_YEAR &&
            this.yearEnd in MIN_YEAR + 1..MAX_YEAR &&
            this.yearStart < this.yearEnd