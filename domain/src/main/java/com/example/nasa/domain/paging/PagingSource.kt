package com.example.nasa.domain.paging

import com.example.nasa.domain.model.LceState
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.MAX_YEAR
import com.example.nasa.domain.util.MIN_YEAR
import kotlinx.coroutines.flow.Flow

interface PagingSource {

    fun onLoadMore(
        searchQuery: String = DEFAULT_SEARCH_QUERY,
        yearStart: Int = MIN_YEAR,
        yearEnd: Int = MAX_YEAR
    )

    fun onRefresh(searchQuery: String, yearStart: Int, yearEnd: Int)

    fun onStopLoading()

    fun getNasaImagePage(): Flow<LceState<List<NasaImage>>>
}