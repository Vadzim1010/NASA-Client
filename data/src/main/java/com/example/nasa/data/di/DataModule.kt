package com.example.nasa.data.di

import org.koin.dsl.module

val dataModule = module {
    includes(
        databaseModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        pagingModule,
    )
}