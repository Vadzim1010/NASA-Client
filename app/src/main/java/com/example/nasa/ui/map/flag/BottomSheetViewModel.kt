package com.example.nasa.ui.map.flag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.domain.usecase.GetCountryDescUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn

class BottomSheetViewModel(
    private val getCountryDescUseCase: GetCountryDescUseCase,
    private val name: String,
) : ViewModel() {

    private val loadFlow = MutableStateFlow(Unit)

    val getCountryDescFlow = loadFlow
        .flatMapLatest {
            getCountryDescUseCase(name)
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )
}
