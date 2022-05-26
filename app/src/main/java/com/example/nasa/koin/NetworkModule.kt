package com.example.nasa.koin

import com.example.nasa.network.ApiConfig
import com.example.nasa.network.NasaApi
import com.example.nasa.network.NasaImagesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val networkModule = module {

    single {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(httpLoginInterceptor)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.NASA_IMAGES_ENDPOINT)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create<NasaImagesApi>()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConfig.NASA_ENDPOINT)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create<NasaApi>()
    }
}