package com.example.nasa.koin

import com.example.nasa.paging.PagingSource
import com.example.nasa.repository.impl.LocalNasaImagesRepositoryImpl
import com.example.nasa.repository.impl.RemoteNasaImagesRepositoryImpl
import org.koin.dsl.module

val pagingModule = module {
    single {
        PagingSource(get<RemoteNasaImagesRepositoryImpl>(), get<LocalNasaImagesRepositoryImpl>())
    }
}