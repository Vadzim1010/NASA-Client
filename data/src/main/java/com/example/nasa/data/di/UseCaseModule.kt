package com.example.nasa.data.di

import com.example.nasa.domain.usecase.GetApodUseCase
import com.example.nasa.domain.usecase.GetDescriptionUseCase
import com.example.nasa.domain.usecase.GetImagePageUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val useCaseModule = module {
    singleOf(::GetImagePageUseCase)
    singleOf(::GetDescriptionUseCase)
    singleOf(::GetApodUseCase)
}