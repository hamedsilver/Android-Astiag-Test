package com.mkdev.astiagtestapp.di

import androidx.annotation.Keep
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.viewModels.MainFragmentViewModel
import com.mkdev.astiagtestapp.views.ui.activities.MainActivity
import dagger.Component
import javax.inject.Singleton

@Keep
@Singleton
@Component(modules = [AppModule::class])
interface DIComponent {

    interface Injectable {
        fun inject(diComponent: DIComponent)
    }

    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(mainActivity: MainActivity)
}