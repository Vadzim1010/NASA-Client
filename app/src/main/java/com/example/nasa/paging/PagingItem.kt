package com.example.nasa.paging

sealed class PagingItem<out T> {

    data class Content<T>(val data: T) : PagingItem<T>()

    data class Error(val throwable: Throwable) : PagingItem<Nothing>()

    object Loading : PagingItem<Nothing>()
}