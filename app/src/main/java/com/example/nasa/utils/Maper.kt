package com.example.nasa.utils

import com.example.nasa.model.PagingItem

val <T> List<T>.mapToPage: List<PagingItem.Content<T>>
    get() = this.map { PagingItem.Content(it) }