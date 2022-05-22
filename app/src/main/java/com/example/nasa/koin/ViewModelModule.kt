package com.example.nasa.koin

import com.example.nasa.ui.image.NasaImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        NasaImageViewModel(get())
    }
}