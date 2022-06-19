package com.example.nasa.data.di

import com.example.nasa.data.service.location.LocationService
import com.example.nasa.data.service.nigtmode.NightModeServiceImpl
import com.example.nasa.domain.service.NightModeService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val serviceModule = module {
    singleOf(::LocationService)
    singleOf(::NightModeServiceImpl) { bind<NightModeService>() }
}