package com.example.nasa

import android.app.Application
import com.example.nasa.data.di.dataModule
import com.example.nasa.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class NasaApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NasaApplication)
            modules(
                viewModelModule,
                dataModule,
            )
        }
    }
}