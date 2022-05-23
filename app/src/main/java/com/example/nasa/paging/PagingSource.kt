package com.example.nasa.paging

import com.example.nasa.model.LceState
import com.example.nasa.model.NasaImage
import com.example.nasa.model.SearchParams
import com.example.nasa.repository.LocalNasaImagesRepository
import com.example.nasa.repository.RemoteNasaImagesRepository
import com.example.nasa.utils.LAST_PAGE
import com.example.nasa.utils.PAGE_LIMIT
import com.example.nasa.utils.log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class PagingSource(
    private val remoteRepository: RemoteNasaImagesRepository,
    private val localRepository: LocalNasaImagesRepository,
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

                        LceState.Content(currentList, hasMoreToLoad)
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

                        LceState.Error(currentList, throwable)
                    }
                )
        }
        .onEach {
            isLoading = false
            log("isLoading: $isLoading")
        }
        .onStart {
            emit(LceState.Loading)

            val cacheList = localRepository.getImagePage(currentPage, searchParams)

            if (cacheList.isNotEmpty()) {
                log("load cache")
                emit(LceState.Content(cacheList, hasMoreToLoad))
            }
        }
}

enum class LoadState {
    LOAD_MORE, REFRESH
}