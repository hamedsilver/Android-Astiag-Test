package com.mkdev.astiagtestapp.di

import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.utils.NavigationEvent
import dagger.Module
import dagger.Provides
import io.reactivex.processors.PublishProcessor
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    fun providesApp(): App = app

    @Provides
    @Singleton
    fun providesNavEventProcessor(): PublishProcessor<NavigationEvent> = PublishProcessor.create()
}