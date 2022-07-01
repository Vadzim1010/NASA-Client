package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.paging.PagingSource
import com.example.nasa.domain.service.SearchParamsService
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class NasaImageViewModel(
    private val pagingSource: PagingSource,
    private val searchParamsService: SearchParamsService,
) : ViewModel() {

    private var searchParams by searchParamsService::searchParams

    private val _pagingSourceFlow = MutableSharedFlow<Resource<List<NasaImage>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val pagingSourceFlow = _pagingSourceFlow.asSharedFlow()

    init {
        pagingSource.onLoadMore(
            searchQuery = searchParams.query,
            yearStart = searchParams.startYear,
            yearEnd = searchParams.endYear,
        )

        viewModelScope.launch {
            _pagingSourceFlow.emitAll(pagingSource.getNasaImagePage())
        }
    }

    fun onReload() {
        pagingSource.onReload(
            searchQuery = searchParams.query,
            yearStart = searchParams.startYear,
            yearEnd = searchParams.endYear,
        )
    }

    fun onRefresh() {
        pagingSource.onRefresh(
            searchQuery = searchParams.query,
            yearStart = searchParams.startYear,
            yearEnd = searchParams.endYear,
        )
    }

    fun onLoadMore() {
        pagingSource.onLoadMore(
            searchQuery = searchParams.query,
            yearStart = searchParams.startYear,
            yearEnd = searchParams.endYear,
        )
    }
}
