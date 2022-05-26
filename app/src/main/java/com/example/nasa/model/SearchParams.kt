package com.example.nasa.model

import com.example.nasa.utils.MAX_YEAR
import com.example.nasa.utils.MIN_YEAR

data class SearchParams(
    var search: String = "",
    var yearStart: Int = MIN_YEAR,
    var yearEnd: Int = MAX_YEAR,
)
