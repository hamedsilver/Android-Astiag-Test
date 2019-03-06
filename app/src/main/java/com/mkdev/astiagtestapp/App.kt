package com.mkdev.astiagtestapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.mkdev.astiagtestapp.di.ApiModule
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

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // new activity created; force its orientation to portrait
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })

        di = DaggerDIComponent
            .builder()
                .apiModule(ApiModule())
            .appModule(AppModule(this))
            .build()
    }
}