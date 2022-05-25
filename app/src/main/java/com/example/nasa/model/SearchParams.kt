package com.example.nasa.model

data class SearchParams(
    var search: String = "",
    var yearStart: Int = 1920,
    var yearEnd: Int = 2022,
)
