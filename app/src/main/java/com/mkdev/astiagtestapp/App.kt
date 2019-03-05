package com.mkdev.astiagtestapp

import android.app.Application
import android.content.Context
import com.mkdev.astiagtestapp.di.AppModule
import com.mkdev.astiagtestapp.di.DIComponent
import com.mkdev.astiagtestapp.di.DaggerDIComponent

class App : Application() {

    lateinit var di: DIComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        di = DaggerDIComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}