package com.example.nasa

import android.app.Application
import com.example.nasa.koin.*
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
                databaseModule,
            )
        }
    }
}