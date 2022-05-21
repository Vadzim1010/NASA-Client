package com.example.nasa.model

import com.example.nasa.paging.PagingItem

data class NasaImage(
    val id: String,
    val imageUrl: String
) {

    fun toPagingItem() = PagingItem.Content(this)
}
