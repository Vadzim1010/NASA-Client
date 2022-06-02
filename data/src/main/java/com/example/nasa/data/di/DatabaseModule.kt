package com.example.nasa.data.di

import androidx.room.Room
import com.example.nasa.data.database.NasaDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val databaseModule = module {
    single {
        Room
            .databaseBuilder(
                get(),
                NasaDatabase::class.java,
                "nasa_database"
            )
            .build()
    }

    single {
        get<NasaDatabase>().getImageDao()
    }
}