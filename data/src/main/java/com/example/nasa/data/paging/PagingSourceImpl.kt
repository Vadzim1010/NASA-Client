package com.example.nasa.data.paging

import com.example.nasa.data.model.LoadState
import com.example.nasa.data.util.log
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.paging.PagingSource
import com.example.nasa.domain.usecase.GetImagePageUseCase
import com.example.nasa.domain.util.MAX_PAGE
import com.example.nasa.domain.util.PAGE_SIZE
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach

internal class PagingSourceImpl(
    private val getImagePageUseCase: GetImagePageUseCase
) : PagingSource {

    private val loadStateFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var cacheList = listOf<NasaImage>()

    private var currentPage = 1

    private var isLoading = false

    private var isLastPage = false

    override fun onLoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) {
        if (!isLoading && !isLastPage) {
            loadStateFlow.tryEmit(
                LoadState.LoadMore(
                    searchQuery = searchQuery,
                    yearStart = yearStart,
                    yearEnd = yearEnd,
                )
            )
        }
    }

    override fun onRefresh(searchQuery: String, yearStart: Int, yearEnd: Int) {
        if (!isLoading) {
            loadStateFlow.tryEmit(
                LoadState.Refresh(
                    searchQuery = searchQuery,
                    yearStart = yearStart,
                    yearEnd = yearEnd,
                )
            )
        }
    }

    override fun onReload(searchQuery: String, yearStart: Int, yearEnd: Int) {
        if (!isLoading) {
            loadStateFlow.tryEmit(
                LoadState.Reload(
                    searchQuery = searchQuery,
                    yearStart = yearStart,
                    yearEnd = yearEnd,
                )
            )
        }
    }

    override fun getNasaImagePage(): Flow<Resource<List<NasaImage>>> = loadStateFlow
        .onEach { loadState ->
            isLoading = true

            if (loadState is LoadState.Reload) {
                currentPage = 1
            }

            log("page $currentPage")
            log("search params is: ${loadState.searchQuery} ${loadState.yearStart} ${loadState.yearEnd}")
        }
        .flatMapLatest { loadState ->
            getImagePageUseCase(
                page = currentPage,
                query = loadState.searchQuery,
                startYear = loadState.yearStart,
                endYear = loadState.yearEnd,
            )
        }
        .onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    cacheList = emptyList()
                    cacheList = resource.data ?: emptyList()

                    isLastPage = false
                    if (cacheList.size < PAGE_SIZE * currentPage || currentPage == MAX_PAGE) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }

                    isLoading = false

                    log("load internet success")
                }
                is Resource.Error -> {
                    cacheList = emptyList()
                    cacheList = resource.data ?: emptyList()

                    isLastPage = false
                    if (cacheList.size < PAGE_SIZE * currentPage || currentPage == MAX_PAGE) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }

                    isLoading = false

                    log("load internet failed")
                }
                is Resource.Loading -> {
                    cacheList = emptyList()
                    cacheList = resource.data ?: emptyList()

                    log("load cache from db")
                }
            }
        }
}
