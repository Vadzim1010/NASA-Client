package com.example.nasa.data.model

import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_START_YEAR

sealed class LoadState(
    val searchQuery: String = DEFAULT_SEARCH_QUERY,
    val yearStart: Int = DEFAULT_END_YEAR,
    val yearEnd: Int = DEFAULT_START_YEAR
) {

    class LoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) :
        LoadState(searchQuery, yearStart, yearEnd)

    class Refresh(searchQuery: String, yearStart: Int, yearEnd: Int) :
        LoadState(searchQuery, yearStart, yearEnd)

    class Reload(searchQuery: String, yearStart: Int, yearEnd: Int) :
        LoadState(searchQuery, yearStart, yearEnd)
}
