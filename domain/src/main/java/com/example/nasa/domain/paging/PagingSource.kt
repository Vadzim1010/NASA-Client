package com.example.nasa.domain.paging

import com.example.nasa.domain.model.LceState
import com.example.nasa.domain.model.NasaImage
import kotlinx.coroutines.flow.Flow

interface PagingSource {

    fun onLoadMore()

    fun onRefresh()

    fun onStopLoading()

    fun getNasaImagePage(): Flow<LceState<List<NasaImage>>>

    fun setStartYear(startYear: Int)

    fun setEndYear(endYear: Int)

    fun setSearchQuery(searchQuery: String)
}