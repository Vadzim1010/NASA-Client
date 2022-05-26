package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.model.Description
import com.example.nasa.repository.RemoteRepository
import com.example.nasa.utils.Resource
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class DescriptionViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {


    suspend fun fetchDescription(nasaId: String) = flow<Resource<Description>> {
        remoteRepository
            .fetchDescription(nasaId)
            .fold(
                onSuccess = { emit(Resource.Success(it)) },
                onFailure = { emit(Resource.Error(throwable = it)) },
            )
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1,
    )
        .onStart {
            emit(Resource.Loading())
        }
}