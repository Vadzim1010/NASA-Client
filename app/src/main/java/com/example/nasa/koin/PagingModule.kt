package com.example.nasa.koin

import com.example.nasa.paging.PagingSource
import org.koin.dsl.module

val pagingModule = module {
    single {
        PagingSource(get(), get())
    }
}