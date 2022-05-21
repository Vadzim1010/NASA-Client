package com.example.nasa.paging

import com.example.nasa.model.LceState
import com.example.nasa.model.NasaImage
import com.example.nasa.repository.LocalRepository
import com.example.nasa.repository.RemoteRepository
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

    private val _pagingSourceFlow = MutableSharedFlow<List<NasaImage>>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val pagingSourceFlow = _pagingSourceFlow.asSharedFlow()

    private var currentList = listOf<NasaImage>()
    private var currentPage = 1
    private var isLoading = false


    fun onLoadMore() {
        loadStateFlow.tryEmit(LoadState.LOAD_MORE)
    }

    fun onRefresh() {
        loadStateFlow.tryEmit(LoadState.REFRESH)
    }

    fun getNasaImagePage() = loadStateFlow
        .filter { !isLoading }
        .onEach {
            isLoading = true
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
            remoteRepository.fetchNasaImages(currentPage)
                .fold(
                    onSuccess = { networkPageList ->
                        localRepository.insertImagePage(currentPage, networkPageList)

                        currentPage++

                        currentList = currentList + networkPageList

                        LceState.Content(currentList)
                    },
                    onFailure = { throwable ->
                        val cacheList = localRepository.getImagePage(currentPage)

                        if (cacheList.isNotEmpty()) {
                            currentPage++
                        }

                        currentList = currentList + cacheList

                        LceState.Error(currentList, throwable)
                    }
                )
        }
        .onEach {
            isLoading = false
        }
        .onStart {
            emit(LceState.Loading)
        }
}

enum class LoadState {
    LOAD_MORE, REFRESH
}