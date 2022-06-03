package com.example.nasa.ui.image

import androidx.lifecycle.ViewModel
import com.example.nasa.domain.paging.PagingSource


class NasaImageViewModel(private val pagingSource: PagingSource) : ViewModel() {


    init {
        pagingSource.onLoadMore()
    }


    fun onStopLoading() {
        pagingSource.onStopLoading()
    }

    fun onRefresh(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onRefresh(searchQuery, yearStart, yearEnd)
    }

    fun onLoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) {
        pagingSource.onLoadMore(searchQuery, yearStart, yearEnd)
    }

    fun getImagesPagingSource() =
        pagingSource.getNasaImagePage()
}

