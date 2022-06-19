package com.example.nasa.ui.settings

import androidx.lifecycle.ViewModel
import com.example.nasa.domain.service.NightModeService

class SettingsViewModel(private val nightModeService: NightModeService) : ViewModel() {

    var nightMode by nightModeService::nightMode
}