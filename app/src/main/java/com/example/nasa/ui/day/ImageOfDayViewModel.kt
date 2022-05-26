package com.example.nasa.ui.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class ImageOfDayViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    fun fetchPictureOfDay() = flow {
        val resource = remoteRepository.fetchPictureOfDay()
            .fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { Resource.Error(throwable = it) }
            )

        emit(resource)
    }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )
        .onStart {
            emit(Resource.Loading())
        }
}