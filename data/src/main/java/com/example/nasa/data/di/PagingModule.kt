package com.example.nasa.data.di


import com.example.nasa.data.paging.PagingSourceImpl
import com.example.nasa.domain.paging.PagingSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val pagingModule = module {
    singleOf(::PagingSourceImpl) { bind<PagingSource>() }
}