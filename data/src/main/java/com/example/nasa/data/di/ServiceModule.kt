package com.example.nasa.data.di

import com.example.nasa.data.service.location.LocationService
import com.example.nasa.data.service.prefs.PrefsServiceImpl
import com.example.nasa.domain.service.NightModeService
import com.example.nasa.domain.service.SearchParamsService
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val serviceModule = module {
    singleOf(::LocationService)
    singleOf(::PrefsServiceImpl) {
        bind<NightModeService>()
        bind<SearchParamsService>()
    }
}
