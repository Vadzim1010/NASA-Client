package com.example.nasa.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.data.service.location.LocationService
import com.example.nasa.domain.model.CountryFlag
import com.example.nasa.domain.usecase.GetCountriesUseCase
import com.example.nasa.domain.usecase.GetCountryDescUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

@SuppressLint("MissingPermission")
class MapViewModel(
    private val locationService: LocationService,
    private val getCountriesUseCase: GetCountriesUseCase,
) : ViewModel() {


    private val countriesFlow = MutableStateFlow(Unit)

    private val locationFlow = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1,
    )


    fun getCurrentLocationFlow() =
        locationFlow
            .mapLatest {
                locationService.getCurrentLocation()
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                replay = 1
            )

    fun getCountriesFLow() = countriesFlow
        .flatMapLatest {
            getCountriesUseCase()
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    fun loadCurrentLocation() {
        locationFlow.tryEmit(Unit)
    }

    fun getLocationFlow() = locationService.getLocationFlow()
}