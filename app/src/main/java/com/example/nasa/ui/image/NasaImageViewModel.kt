package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import com.example.nasa.paging.PagingSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


    fun onRefresh() {
        pagingSource.onRefresh()
    }

    fun onLoadMore() {
        pagingSource.onLoadMore()
    }

    fun getImagesPagingSource() = pagingSource.getNasaImagePage()
}

