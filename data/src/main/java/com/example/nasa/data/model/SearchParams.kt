package com.example.nasa.data.model

import com.example.nasa.data.util.MAX_YEAR
import com.example.nasa.data.util.MIN_YEAR

internal data class SearchParams(
    var searchQuery: String = "",
    var yearStart: Int = MIN_YEAR,
    var yearEnd: Int = MAX_YEAR,
)
