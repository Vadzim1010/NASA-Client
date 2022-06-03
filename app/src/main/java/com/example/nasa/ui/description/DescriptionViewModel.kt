package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class DescriptionViewModel(private val getDescriptionUseCase: GetDescriptionUseCase) : ViewModel() {


    fun fetchDescription(nasaId: String) =
        getDescriptionUseCase(nasaId)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1,
            )
}