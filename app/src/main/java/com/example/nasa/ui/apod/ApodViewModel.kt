package com.example.nasa.ui.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.usecase.GetApodUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class ApodViewModel(private val getApodUseCase: GetApodUseCase) : ViewModel() {

    fun fetchPictureOfDay() = flow {
        val resource = getApodUseCase()
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