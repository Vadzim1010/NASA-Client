package com.example.nasa.koin

import androidx.room.Room
import com.example.nasa.database.NasaDatabase
import org.koin.dsl.module

val databaseModule = module {
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