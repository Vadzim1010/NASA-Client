package com.example.nasa.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasa.data.service.location.LocationService
import com.example.nasa.domain.usecase.GetCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn

@SuppressLint("MissingPermission")
class MapViewModel(
    private val locationService: LocationService,
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private val countriesFlow = MutableStateFlow(Unit)

    fun getCountriesFLow() = countriesFlow
        .mapLatest {
            getCountriesUseCase()
                .fold(onSuccess = { it }, onFailure = { emptyList() })
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    suspend fun getCurrentLocation() = locationService.getCurrentLocation()

    fun getLocationFlow() = locationService.getLocationFlow()
}