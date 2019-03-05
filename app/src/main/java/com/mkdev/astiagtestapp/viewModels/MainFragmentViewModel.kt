package com.mkdev.astiagtestapp.viewModels

import androidx.lifecycle.ViewModel
import com.mkdev.astiagtestapp.di.DIComponent
import com.mkdev.astiagtestapp.utils.NavigationEvent
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class MainFragmentViewModel : ViewModel(), DIComponent.Injectable {
    override fun inject(diComponent: DIComponent) {
        diComponent.inject(this)
    }

    @Inject
    lateinit var navEvents: PublishProcessor<NavigationEvent>

    fun openNavigation() {
        navEvents.onNext(NavigationEvent(NavigationEvent.NavEvent.OPEN_DRAWER))
    }
}