package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import com.example.nasa.paging.PagingSource

class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


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

