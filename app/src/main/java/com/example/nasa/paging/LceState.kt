package com.example.nasa.paging

sealed class LceState<T> {

    data class Content<T>(val data: T) : LceState<T>()

    data class Error(val throwable: Throwable) : LceState<Nothing>()

    object Loading : LceState<Nothing>()
}