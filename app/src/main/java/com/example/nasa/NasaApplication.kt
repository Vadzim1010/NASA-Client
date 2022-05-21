package com.example.nasa

import android.app.Application
import com.example.nasa.koin.networkModule
import com.example.nasa.koin.pagingModule
import com.example.nasa.koin.repositoryModule
import com.example.nasa.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NasaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NasaApplication)
            modules(
                networkModule,
                repositoryModule,
                pagingModule,
                viewModelModule,
            )
        }
    }
}