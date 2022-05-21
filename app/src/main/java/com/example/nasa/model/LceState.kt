package com.example.nasa.model

sealed class LceState<out T> {

    data class Content<T>(val data: T) : LceState<T>()

    data class Error<T>(val data: T, val throwable: Throwable) : LceState<T>()

    object Loading : LceState<Nothing>()
}