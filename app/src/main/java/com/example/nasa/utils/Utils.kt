package com.example.nasa.utils

import android.util.Log
import com.example.nasa.database.entity.NasaImageEntity
import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams
import com.example.nasa.network.model.NasaResponse
import com.example.nasa.paging.PagingItem
import java.util.*


val <T> List<T>.mapToPage: List<PagingItem.Content<T>>
    get() = this.map { PagingItem.Content(it) }

val List<NasaImageEntity>.mapToModel: List<NasaImage>
    get() = this.map { it.toModel() }

val NasaResponse.mapToModel: List<NasaImage>
    get() = this
        .collection
        .items.map { item ->
            NasaImage(
                id = item.data.getOrNull(0)?.nasaId ?: UUID.randomUUID().toString(),
                imageUrl = item.links.getOrNull(0)?.href ?: "",
            )
        }


fun List<NasaImage>.mapToEntity(page: Int, searchParams: SearchParams) =
    this.map { it.toEntity(page, searchParams) }

fun log(message: String) {
    Log.i("TAG", message)
}

val SearchParams.checkFormat: Boolean
    get() = this.yearStart in MIN_YEAR until MAX_YEAR &&
            this.yearEnd in MIN_YEAR + 1..MAX_YEAR &&
            this.yearStart < this.yearEnd