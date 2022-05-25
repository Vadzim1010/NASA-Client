package com.example.nasa.utils

sealed class Resource<out T> {

    data class Success<T>(val data: T, val isLastPage: Boolean = false) : Resource<T>()

    data class Error<T>(val data: T? = null, val throwable: Throwable) : Resource<T>()

    data class Loading<T>(val data: T? = null) : Resource<T>()
}