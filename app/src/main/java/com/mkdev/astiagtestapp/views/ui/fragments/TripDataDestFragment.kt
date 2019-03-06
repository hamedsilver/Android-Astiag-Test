package com.mkdev.astiagtestapp.views.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.models.LocationModel
import com.mkdev.astiagtestapp.utils.*
import com.mkdev.astiagtestapp.viewModels.TripDataFragmentViewModel
import com.mkdev.astiagtestapp.viewModels.ViewModelFactory
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_trip_data_dest.*
import timber.log.Timber

private const val ARG_PARAM1 = "param1"

class TripDataDestFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v) {
            tvAccept -> {
                MainFragment.mainActionsPublisher.onNext(Pair(MainFragment.ActionType.ACCEPT_DESTINATION, currentAddress))
            }
        }
    }

    private val viewDestroyCompositeDisposable = CompositeDisposable()
    private val destroyCompositeDisposable = CompositeDisposable()

    private lateinit var viewModel: TripDataFragmentViewModel

    private lateinit var sourceAddress: LocationModel
    private lateinit var currentAddress: LocationModel

    companion object {
        var tripDataActionsPublisher: PublishProcessor<Pair<MainFragment.ActionType, Any?>> =
                PublishProcessor.create()

        fun newInstance(address: LocationModel) = TripDataDestFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PARAM1, address)
            }
        }
    }

    override fun initBeforeView() {
        with(context!!.applicationContext as App) {
            viewModel = ViewModelProviders.of(this@TripDataDestFragment,
                    ViewModelFactory(this)).get(TripDataFragmentViewModel::class.java)
        }

        arguments?.let {
            sourceAddress = it.getParcelable(ARG_PARAM1)!!
        }
    }

    override fun getContentViewId(): Int = R.layout.fragment_trip_data_dest

    override fun initViews(rootView: View) {

        tvAccept.setOnClickListener(this)
        tvAccept.changeState(ViewType.DISABLE, R.drawable.rectangle_shape_fill_dark)

        tvOriginTitle.text = sourceAddress.getMainAddress()
        tvOriginSubTitle.text = sourceAddress.getSubAddress()

        tripDataActionsPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.first) {
                        MainFragment.ActionType.SHOW_LOCATION_DATA -> {
                            getCurrentLocationData(it.second as LatLng)
                        }
                    }
                }.addTo(viewDestroyCompositeDisposable)
    }

    private fun getCurrentLocationData(loc: LatLng) {
        viewModel.getCurrentLocationData(loc)
                .iomain()
                .doOnSubscribe {
                    tvAccept.changeState(ViewType.DISABLE, R.drawable.rectangle_shape_fill_dark)
                    tvDestinationTitle.text = getString(R.string.loading_address)
                    tvDestinationSubTitle.gone()
                }.subscribe({
                    currentAddress = it
                    tvDestinationTitle.text = it?.getMainAddress()
                    tvDestinationSubTitle.text = it?.getSubAddress()

                    tvAccept.changeState(ViewType.ENABLE, R.drawable.rectangle_shape_fill_orange)
                    tvDestinationSubTitle.visible()
                }, {
                    Timber.e(it)
                    CustomToast.makeText(context!!, getString(R.string.server_error), CustomToast.ERROR)
                }).addTo(viewDestroyCompositeDisposable)
    }

    override fun onDestroy() {
        destroyCompositeDisposable.clear()
        super.onDestroy()
    }

    override fun onDestroyView() {
        viewDestroyCompositeDisposable.clear()
        super.onDestroyView()
    }
}
