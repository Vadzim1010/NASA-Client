package com.example.nasa.ui.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.usecase.GetApodUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class ApodViewModel(getApodUseCase: GetApodUseCase) : ViewModel() {

    val apodFlow = getApodUseCase()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )
}