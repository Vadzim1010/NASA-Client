package com.example.nasa.di

import com.example.nasa.ui.description.DescriptionViewModel
import com.example.nasa.ui.image.NasaImageViewModel
import com.example.nasa.ui.image.search.BottomSheetSearchViewModel
import com.example.nasa.ui.map.MapViewModel
import com.example.nasa.ui.map.flag.BottomSheetViewModel
import com.example.nasa.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NasaImageViewModel)
    viewModelOf(::DescriptionViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::BottomSheetViewModel)
    viewModelOf(::BottomSheetSearchViewModel)
}