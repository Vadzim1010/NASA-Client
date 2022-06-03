package com.example.nasa.data.di

import androidx.room.Room
import com.example.nasa.data.database.NasaDatabase
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

    single {
        get<NasaDatabase>().getDescriptionDao()
    }

    single {
        get<NasaDatabase>().getImageDescriptionDao()
    }

    single {
        get<NasaDatabase>().getApodDao()
    }
}