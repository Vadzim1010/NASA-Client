package com.example.nasa.domain.model


sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {

    class Success<T>(data: T, val hasMoreData: Boolean = true) : Resource<T>(data)

    class Loading<T>(data: T? = null) : Resource<T>(data)

    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}