package com.example.nasa.domain.model

sealed class LceState<out T>() {

    data class Content<T>(val data: T, val hasMoreData: Boolean = true) : LceState<T>()

    data class Error<T>(val throwable: Throwable, val data: T? = null) : LceState<T>()

    data class Loading<T>(val data: T? = null) : LceState<T>()
}
