package com.example.nasa.data.di

import com.example.nasa.data.BuildConfig
import com.example.nasa.data.network.api.CountriesApi
import com.example.nasa.data.network.api.NasaImagesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

internal val networkModule = module {
    single {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(httpLoginInterceptor)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
    }

    single {
        get<Retrofit.Builder>()
            .baseUrl(BuildConfig.NASA_IMAGES_ENDPOINT)
            .build()
            .create<NasaImagesApi>()
    }

    single {
        get<Retrofit.Builder>()
            .baseUrl(BuildConfig.COUNTRIES_ENDPOINT)
            .build()
            .create<CountriesApi>()
    }
}
