package com.mkdev.astiagtestapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.mkdev.astiagtestapp.api.Api
import com.mkdev.astiagtestapp.di.DIComponent
import com.mkdev.astiagtestapp.models.LocationModel
import com.mkdev.astiagtestapp.utils.NavigationEvent
import io.reactivex.Single
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

class TripDataFragmentViewModel : ViewModel(), DIComponent.Injectable {
    override fun inject(diComponent: DIComponent) {
        diComponent.inject(this)
    }

    @Inject
    lateinit var navEvents: PublishProcessor<NavigationEvent>

    @Inject
    lateinit var api: Api

    fun openNavigation() {
        navEvents.onNext(NavigationEvent(NavigationEvent.NavEvent.OPEN_DRAWER))
    }

    fun getCurrentLocationData(loc: LatLng): Single<LocationModel> =
            api.getCurrentLocationData("jsonv2", loc.latitude, loc.longitude)

}