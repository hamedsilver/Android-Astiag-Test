package com.mkdev.astiagtestapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.BuildConfig
import com.mkdev.astiagtestapp.Constants
import com.mkdev.astiagtestapp.api.Api
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesRetrofit(app: App): Retrofit =
            Retrofit.Builder().baseUrl(Constants.BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder()
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor {
                                val request = it.request()
                                val response = it.proceed(request)

                                when (response.code()) {
                                    400 -> {
                                        Timber.d("400 - Bad Request")
                                    }
                                    401 -> {
                                        Timber.d("401 - Unauthorized - refresh token")
                                    }
                                    403 -> {
                                        Timber.d("403 - Forbidden")
                                    }
                                    404 -> {
                                        Timber.d("404 - Not Found - URL")
                                    }
                                    405 -> {
                                        Timber.d("405 - Method Not Allowed - DELETE - GET")
                                    }
                                    500 -> {
                                        Timber.d("500 - Internal Server ErrorT")
                                    }
                                    else -> {
                                        Timber.d("Server error - ${response.message()}")
                                    }
                                }

                                response
                            }
                            .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE))
                            .build()).build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}