package com.example.nasa.domain.paging

import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_START_YEAR
import kotlinx.coroutines.flow.Flow

interface PagingSource {

    fun onLoadMore(
        searchQuery: String = DEFAULT_SEARCH_QUERY,
        yearStart: Int = DEFAULT_START_YEAR,
        yearEnd: Int = DEFAULT_END_YEAR
    )

    fun onReload(
        searchQuery: String = DEFAULT_SEARCH_QUERY,
        yearStart: Int = DEFAULT_START_YEAR,
        yearEnd: Int = DEFAULT_END_YEAR
    )

    fun onRefresh(
        searchQuery: String = DEFAULT_SEARCH_QUERY,
        yearStart: Int = DEFAULT_START_YEAR,
        yearEnd: Int = DEFAULT_END_YEAR
    )

    fun getNasaImagePage(): Flow<Resource<List<NasaImage>>>
}