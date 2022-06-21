package com.example.nasa.ui.flag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.data.util.log
import com.example.nasa.domain.usecase.GetCountryDescUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn

class BottomSheetViewModel(
    private val getCountryDescUseCase: GetCountryDescUseCase,
    private val name: String,
) : ViewModel() {

    private val countyDescFlow = MutableStateFlow(Unit)

    fun getCountryDescFlow() =
        countyDescFlow
            .flatMapLatest {
                getCountryDescUseCase(name)
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                replay = 1
            )
}