package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.model.SearchParams
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


    private val _pagingSourceFlow = MutableSharedFlow<Resource<List<NasaImage>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val pagingSourceFlow = _pagingSourceFlow.asSharedFlow()

    var searchParams by searchParamsService::searchParams


    init {
        pagingSource.onReload(searchParams.query, searchParams.startYear, searchParams.endYear)

        viewModelScope.launch {
            _pagingSourceFlow.emitAll(pagingSource.getNasaImagePage())
        }
    }


    fun onReload(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onReload(searchQuery, yearStart, yearEnd)
    }

    fun onRefresh(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onRefresh(searchQuery, yearStart, yearEnd)
    }

    fun onLoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onLoadMore(searchQuery, yearStart, yearEnd)
    }
}

