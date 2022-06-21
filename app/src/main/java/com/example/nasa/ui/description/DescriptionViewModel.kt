package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import kotlinx.coroutines.flow.*

class DescriptionViewModel(
    private val getDescriptionUseCase: GetDescriptionUseCase,
    private val id: String,
) : ViewModel() {

    private val descriptionFlow = MutableStateFlow(Unit)

    fun getDescriptionFlow() =
        descriptionFlow
            .flatMapLatest { getDescriptionUseCase(id) }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1
            )
}