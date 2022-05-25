package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.model.SearchParams
import com.example.nasa.paging.PagingSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


    fun onRefresh() {
        pagingSource.onRefresh()
    }

    fun onLoadMore() {
        pagingSource.onLoadMore()
    }

    fun getImagesPagingSource(searchParams: SearchParams) = pagingSource
        .getNasaImagePage(searchParams)
}

