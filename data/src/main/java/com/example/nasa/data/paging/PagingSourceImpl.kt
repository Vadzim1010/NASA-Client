package com.example.nasa.data.paging

import com.example.nasa.data.model.LoadState
import com.example.nasa.data.util.log
import com.example.nasa.domain.model.LceState
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.paging.PagingSource
import com.example.nasa.domain.usecase.GetImagePageUseCase
import com.example.nasa.domain.util.MAX_PAGE
import com.example.nasa.domain.util.PAGE_SIZE
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*


internal class PagingSourceImpl(
    private val getImagePageUseCase: GetImagePageUseCase
) : PagingSource {


    private val loadStateFlow = MutableSharedFlow<LoadState>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    private var currentList = listOf<NasaImage>()
    private var cacheSize = 0
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false


    override fun onLoadMore(searchQuery: String, yearStart: Int, yearEnd: Int) {
        if (!isLastPage && !isLoading) {
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
        loadStateFlow.tryEmit(
            LoadState.Refresh(
                searchQuery = searchQuery,
                yearStart = yearStart,
                yearEnd = yearEnd,
            )
        )
    }

    override fun onStopLoading() {
        loadStateFlow.tryEmit(LoadState.Stop)
    }

    override fun getNasaImagePage(): Flow<LceState<List<NasaImage>>> = loadStateFlow
        .filter { it != LoadState.Stop }
        .flatMapLatest { loadState ->
            if (loadState is LoadState.Refresh) {
                currentList = emptyList()
                currentPage = 1
            }

            log("${loadState.searchQuery} ${loadState.yearStart} ${loadState.yearEnd}")

            getImagePageUseCase(
                page = currentPage,
                query = loadState.searchQuery,
                startYear = loadState.yearStart,
                endYear = loadState.yearEnd,
            )
        }
        .onEach {
            isLoading = true
            log("isLoading: $isLoading")
        }
        .onEach {
            log("page $currentPage")
        }
        .map { resource ->
            when (resource) {
                is Resource.Success -> {
                    currentList = currentList.dropLast(cacheSize) + resource.data

                    isLastPage = false
                    if (resource.data.size < PAGE_SIZE || currentPage == MAX_PAGE) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }

                    isLoading = false
                    log("isLoading: $isLoading")
                    log("load internet success")

                    LceState.Content(data = currentList, hasMoreData = isLastPage)
                }
                is Resource.Error -> {
                    val cacheList = resource.data ?: emptyList()

                    currentList = currentList.dropLast(cacheSize) + cacheList

                    isLastPage = false
                    if (cacheList.size < PAGE_SIZE || currentPage == MAX_PAGE) {
                        isLastPage = true
                    } else {
                        currentPage++
                    }

                    isLoading = false
                    log("isLoading: $isLoading")
                    log("load internet success")

                    LceState.Error(throwable = resource.throwable, data = currentList)
                }
                is Resource.Loading -> {
                    val cacheList = resource.data ?: emptyList()
                    currentList = currentList + cacheList

                    cacheSize = resource.data?.size ?: 0

                    log("load cache")
                    LceState.Loading(currentList)
                }
            }
        }
        .onStart {
            emit(LceState.Loading(currentList))
        }
}