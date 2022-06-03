package com.example.nasa.data.di

import com.example.nasa.data.repository.*
import com.example.nasa.data.repository.LocalImagesRepositoryImpl
import com.example.nasa.data.repository.RemoteDescRepositoryImpl
import com.example.nasa.data.repository.RemoteImagesRepositoryImpl
import com.example.nasa.domain.repository.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


internal val repositoryModule = module {
    singleOf(::RemoteImagesRepositoryImpl) { bind<RemoteImagesRepository>() }
    singleOf(::RemoteDescRepositoryImpl) { bind<RemoteDescRepository>() }
    singleOf(::RemoteApodRepositoryImpl) { bind<RemoteApodRepository>() }
    singleOf(::LocalImagesRepositoryImpl) { bind<LocalImagesRepository>() }
    singleOf(::LocalDescRepositoryImpl) { bind<LocalDescRepository>() }
    singleOf(::LocalApodRepositoryImpl) { bind<LocalApodRepository>() }
}