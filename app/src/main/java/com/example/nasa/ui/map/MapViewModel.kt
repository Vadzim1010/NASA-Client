package com.example.nasa.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.data.service.location.LocationService
import com.example.nasa.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.flow.*

class MapViewModel(
    private val locationService: LocationService,
    private val getCountriesUseCase: GetCountriesUseCase,
) : ViewModel() {

    private val loadingFlow = MutableStateFlow(Unit)

    @SuppressLint("MissingPermission")
    val currentLocationFlow = loadingFlow
        .mapLatest { locationService.getCurrentLocation() }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 0
        )

    val countriesFLow = flow { emitAll(getCountriesUseCase()) }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    @SuppressLint("MissingPermission")
    val locationFlow = locationService
        .getLocationFlow()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    fun loadCurrentLocation() {
        loadingFlow.tryEmit(Unit)
    }
}
