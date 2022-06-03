package com.example.nasa.data.model

import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.MAX_YEAR
import com.example.nasa.domain.util.MIN_YEAR


sealed class LoadState(
    val searchQuery: String = DEFAULT_SEARCH_QUERY,
    val yearStart: Int = MAX_YEAR,
    val yearEnd: Int = MIN_YEAR
) {

    class LoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) :
        LoadState(searchQuery, yearStart, yearEnd)

    class Refresh(searchQuery: String, yearStart: Int, yearEnd: Int) :
        LoadState(searchQuery, yearStart, yearEnd)

    object Stop : LoadState()
}