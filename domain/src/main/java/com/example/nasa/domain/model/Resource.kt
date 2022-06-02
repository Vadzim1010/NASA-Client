package com.example.nasa.domain.model


sealed class Resource<T>() {

    data class Success<T>(val data: T, val hasMoreData: Boolean = true) : Resource<T>()

    data class Loading<T>(val data: T? = null) : Resource<T>()

    data class Error<T>(val throwable: Throwable, val data: T? = null) : Resource<T>()
}