package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.paging.PagingSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch


class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


    private val _pagingSourceFlow = MutableSharedFlow<Resource<List<NasaImage>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val pagingSourceFlow = _pagingSourceFlow.asSharedFlow()


    init {
        pagingSource.onLoadMore()

        viewModelScope.launch {
            _pagingSourceFlow.emitAll(pagingSource.getNasaImagePage())
        }
    }


    fun onRefresh(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onRefresh(searchQuery, yearStart, yearEnd)
    }

    fun onLoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onLoadMore(searchQuery, yearStart, yearEnd)
    }
}

