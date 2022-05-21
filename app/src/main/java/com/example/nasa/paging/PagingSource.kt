package com.example.nasa.paging

import com.example.nasa.repository.NasaRepository
import com.example.nasa.utils.log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class PagingSource(private val repository: NasaRepository) {

    private val loadStateFlow = MutableSharedFlow<LoadState>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
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
                currentPage = 0
            }
        }
        .map {
            repository.fetchNasaImages(currentPage)
                .fold(
                    onSuccess = {
                        LceState.Content(it)
                    },
                    onFailure = {
                        LceState.Error(it)
                    }
                )
        }
        .onEach {
            log("page $currentPage")
            currentPage++
            isLoading = false
        }
        .onStart {
            emit(LceState.Loading)
        }
}

enum class LoadState {
    LOAD_MORE, REFRESH
}