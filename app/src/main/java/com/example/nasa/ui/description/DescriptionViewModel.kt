package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.repository.RemoteRepository
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class DescriptionViewModel(private val getDescriptionUseCase: GetDescriptionUseCase) : ViewModel() {


    suspend fun fetchDescription(nasaId: String) = flow<Resource<Description>> {
        getDescriptionUseCase(nasaId)
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