package com.example.nasa.data.paging

import com.example.nasa.data.model.SearchParams
import com.example.nasa.data.util.MAX_PAGE
import com.example.nasa.data.util.PAGE_SIZE
import com.example.nasa.data.util.log
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.LocalRepository
import com.example.nasa.domain.repository.RemoteRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class PagingSource(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {


    private val loadStateFlow = MutableSharedFlow<LoadState>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val searchParams = SearchParams()

    private var currentList = listOf<NasaImage>()
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false


    fun onLoadMore() {
        if (!isLastPage && !isLoading) {
            loadStateFlow.tryEmit(LoadState.LOAD_MORE)
        }
    }

    fun onRefresh() {
        loadStateFlow.tryEmit(LoadState.REFRESH)
    }

    fun onStopLoading() {
        loadStateFlow.tryEmit(LoadState.STOP)
    }

    fun getNasaImagePage() = loadStateFlow
        .filter { it != LoadState.STOP }
        .onEach {
            isLoading = true
            log("isLoading: $isLoading")
        }
        .onEach {
            log(searchParams.toString())
        }
        .onEach {
            if (it == LoadState.REFRESH) {
                currentList = emptyList()
                currentPage = 1
            }
        }
        .onEach {
            log("page $currentPage")
        }
        .map {
            remoteRepository.fetchNasaImages(
                page = currentPage,
                query = searchParams.searchQuery,
                startYear = searchParams.yearStart,
                endYear = searchParams.yearEnd,
            )
                .fold(
                    onSuccess = { networkPageList ->
                        localRepository.insertImagePage(
                            page = currentPage,
                            query = searchParams.searchQuery,
                            startYear = searchParams.yearStart,
                            endYear = searchParams.yearEnd,
                            pageList = networkPageList,
                        )

                        isLastPage = false
                        if (networkPageList.size < PAGE_SIZE || currentPage == MAX_PAGE) {
                            isLastPage = true
                        } else {
                            currentPage++
                        }

                        currentList = currentList + networkPageList

                        log("load network")

                        Resource.Success(currentList, isLastPage)
                    },
                    onFailure = { throwable ->
                        val cacheList = localRepository.getImagePage(
                            page = currentPage,
                            query = searchParams.searchQuery,
                            startYear = searchParams.yearStart,
                            endYear = searchParams.yearEnd,
                        )

                        isLastPage = false
                        if (cacheList.size < PAGE_SIZE || currentPage == MAX_PAGE) {
                            isLastPage = true
                        } else {
                            currentPage++
                        }

                        currentList = currentList + cacheList

                        log("load cache")

                        Resource.Error(data = currentList, throwable = throwable)
                    }
                )
        }
        .onEach {
            isLoading = false
            log("isLoading: $isLoading")
        }
        .onStart {
            val cacheList = currentList
                .takeIf { it.isNotEmpty() } ?: localRepository.getImagePage(
                page = currentPage,
                query = searchParams.searchQuery,
                startYear = searchParams.yearStart,
                endYear = searchParams.yearEnd,
            )

            log("load cache")
            emit(Resource.Loading(cacheList))
        }

    fun setStartYear(startYear: Int) {
        searchParams.yearStart = startYear
    }

    fun setEndYear(endYear: Int) {
        searchParams.yearEnd = endYear
    }

    fun setSearchQuery(searchQuery: String) {
        searchParams.searchQuery = searchQuery
    }
}


enum class LoadState {
    LOAD_MORE, REFRESH, STOP
}