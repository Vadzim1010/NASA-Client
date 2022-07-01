package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import kotlinx.coroutines.flow.*

class DescriptionViewModel(
    getDescriptionUseCase: GetDescriptionUseCase,
    id: String,
) : ViewModel() {

    val descriptionFlow = getDescriptionUseCase(id)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )
}
