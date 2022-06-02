package com.example.nasa.data.di

import com.example.nasa.data.paging.PagingSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val pagingModule = module {
    singleOf(::PagingSource)
}