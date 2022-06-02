package com.example.nasa.data.di

import com.example.nasa.data.repository.LocalRepositoryImpl
import com.example.nasa.data.repository.RemoteRepositoryImpl
import com.example.nasa.domain.repository.LocalRepository
import com.example.nasa.domain.repository.RemoteRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val repositoryModule = module {
    singleOf(::RemoteRepositoryImpl) { bind<RemoteRepository>() }
    singleOf(::LocalRepositoryImpl) { bind<LocalRepository>() }
}