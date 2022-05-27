package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.paging.PagingSource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


    init {
        onLoadMore()
    }

    fun onStopLoading() {
        pagingSource.onStopLoading()
    }

    fun onRefresh() {
        pagingSource.onRefresh()
    }

    fun onLoadMore() {
        pagingSource.onLoadMore()
    }

    fun getImagesPagingSource() = pagingSource
        .getNasaImagePage()

    fun setStartYear(yearStart: Int) {
        pagingSource.setStartYear(yearStart)
    }

    fun setEndYear(yearEnd: Int) {
        pagingSource.setEndYear(yearEnd)
    }

    fun setSearchQuery(searchQuery: String) {
        pagingSource.setSearchQuery(searchQuery)
    }
}

