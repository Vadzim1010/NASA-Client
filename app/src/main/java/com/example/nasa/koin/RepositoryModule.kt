package com.example.nasa.koin

import com.example.nasa.repository.impl.LocalNasaImagesRepositoryImpl
import com.example.nasa.repository.impl.RemoteNasaImagesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single {
        RemoteNasaImagesRepositoryImpl(get())
    }

    single {
        LocalNasaImagesRepositoryImpl(get())
    }
}