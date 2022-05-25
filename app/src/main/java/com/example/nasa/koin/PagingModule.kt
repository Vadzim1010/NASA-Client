package com.example.nasa.koin

import com.example.nasa.paging.PagingSource
import com.example.nasa.repository.impl.LocalRepositoryImpl
import com.example.nasa.repository.impl.RemoteRepositoryImpl
import org.koin.dsl.module

val pagingModule = module {
    single {
        PagingSource(get<RemoteRepositoryImpl>(), get<LocalRepositoryImpl>())
    }
}