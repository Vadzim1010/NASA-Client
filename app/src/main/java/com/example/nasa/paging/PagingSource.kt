package com.example.nasa.paging

import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams
import com.example.nasa.repository.LocalRepository
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.MAX_PAGE
import com.example.nasa.utils.PAGE_SIZE
import com.example.nasa.utils.Resource
import com.example.nasa.utils.log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

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

    fun getNasaImagePage() = loadStateFlow
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
            remoteRepository.fetchNasaImages(currentPage, searchParams)
                .fold(
                    onSuccess = { networkPageList ->
                        localRepository.insertImagePage(currentPage, searchParams, networkPageList)

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
                        val cacheList = localRepository.getImagePage(currentPage, searchParams)

                        isLastPage = false
                        if (cacheList.size < PAGE_SIZE || currentPage == MAX_PAGE) {
                            isLastPage = true
                        } else {
                            currentPage++
                        }

                        currentList = currentList + cacheList

                        log("load cache")

                        Resource.Error(currentList, throwable)
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
                searchParams = searchParams,
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
        searchParams.search = searchQuery
    }
}


enum class LoadState {
    LOAD_MORE, REFRESH
}