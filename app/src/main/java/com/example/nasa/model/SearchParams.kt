package com.example.nasa.model

data class SearchParams(
    val search: String = "",
    val yearStart: Int = 1920,
    val yearEnd: Int = 2022,
)
