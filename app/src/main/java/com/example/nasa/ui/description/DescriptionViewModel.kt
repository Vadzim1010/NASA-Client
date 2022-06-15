package com.example.nasa.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DescriptionViewModel(
    private val getDescriptionUseCase: GetDescriptionUseCase,
    val id: String,
) : ViewModel() {

    private val _descriptionFlow = MutableSharedFlow<Resource<List<Description>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val descriptionFlow = _descriptionFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            _descriptionFlow.emitAll(getDescriptionUseCase(id))
        }
    }
}