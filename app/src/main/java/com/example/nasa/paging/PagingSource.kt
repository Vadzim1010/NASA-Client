package com.example.nasa.paging

import com.example.nasa.utils.Resource
import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams
import com.example.nasa.repository.LocalRepository
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.LAST_PAGE
import com.example.nasa.utils.PAGE_LIMIT
import com.example.nasa.utils.log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class PagingSource(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {


    private val loadStateFlow = MutableSharedFlow<LoadState>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var currentList = listOf<NasaImage>()
    private var currentPage = 1
    private var isLoading = false
    private var hasMoreToLoad = true


    fun onLoadMore() {
        if (hasMoreToLoad && !isLoading) {
            loadStateFlow.tryEmit(LoadState.LOAD_MORE)
        }
    }

    fun onRefresh() {
        loadStateFlow.tryEmit(LoadState.REFRESH)
    }

    fun getNasaImagePage(searchParams: SearchParams) = loadStateFlow
        .onEach {
            isLoading = true
            log("isLoading: $isLoading")
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

                        val contentLength = remoteRepository.getContentLength()
                        log("content length : $contentLength")

                        hasMoreToLoad = true
                        if ((PAGE_LIMIT * currentPage) >= contentLength || currentPage == LAST_PAGE) {
                            hasMoreToLoad = false
                        } else {
                            currentPage++
                        }

                        currentList = currentList + networkPageList

                        log("load network")

                        Resource.Success(currentList, hasMoreToLoad)
                    },
                    onFailure = { throwable ->
                        val cacheList = localRepository.getImagePage(currentPage, searchParams)

                        hasMoreToLoad = true
                        if (cacheList.isEmpty() || currentPage == LAST_PAGE) {
                            hasMoreToLoad = false
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
                .takeIf { it.isNotEmpty() } ?: localRepository
                .getImagePage(currentPage, searchParams)

            log("load cache")
            emit(Resource.Loading(cacheList))
        }
}


enum class LoadState {
    LOAD_MORE, REFRESH
}