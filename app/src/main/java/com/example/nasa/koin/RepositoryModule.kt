package com.example.nasa.koin

import com.example.nasa.repository.impl.LocalRepositoryImpl
import com.example.nasa.repository.impl.RemoteRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single {
        RemoteRepositoryImpl(get() ,get())
    }

    single {
        LocalRepositoryImpl(get())
    }
}