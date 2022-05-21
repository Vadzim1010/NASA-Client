package com.example.nasa.koin

import com.example.nasa.repository.NasaRepository
import com.example.nasa.retrofit.NasaApi
import org.koin.dsl.module

val repositoryModule = module {
    single {
        NasaRepository(get())
    }
}