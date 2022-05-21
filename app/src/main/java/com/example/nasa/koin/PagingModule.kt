package com.example.nasa.koin

import com.example.nasa.paging.PagingSource
import com.example.nasa.repository.NasaRepository
import org.koin.dsl.module

val pagingModule = module {
    single {
        PagingSource(get())
    }
}