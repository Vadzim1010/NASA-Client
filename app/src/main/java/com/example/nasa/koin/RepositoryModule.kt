package com.example.nasa.koin

import com.example.nasa.repository.LocalRepository
import com.example.nasa.repository.RemoteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        RemoteRepository(get())
    }

    single {
        LocalRepository(get())
    }
}