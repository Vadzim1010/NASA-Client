package com.example.nasa.koin

import com.example.nasa.repository.impl.RemoteRepositoryImpl
import com.example.nasa.ui.day.ImageOfDayViewModel
import com.example.nasa.ui.description.DescriptionViewModel
import com.example.nasa.ui.image.NasaImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        NasaImageViewModel(get())
    }

    viewModel {
        DescriptionViewModel(get<RemoteRepositoryImpl>())
    }

    viewModel {
        ImageOfDayViewModel(get<RemoteRepositoryImpl>())
    }
}