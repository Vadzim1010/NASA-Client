package com.example.nasa.domain.model

import java.io.Serializable

data class SearchParams(
    val query: String,
    val startYear: Int,
    val endYear: Int,
) : Serializable