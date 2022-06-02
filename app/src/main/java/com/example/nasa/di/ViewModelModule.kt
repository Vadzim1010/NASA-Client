package com.example.nasa.di

import com.example.nasa.ui.apod.ApodViewModel
import com.example.nasa.ui.description.DescriptionViewModel
import com.example.nasa.ui.image.NasaImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NasaImageViewModel)
    viewModelOf(::DescriptionViewModel)
    viewModelOf(::ApodViewModel)
}