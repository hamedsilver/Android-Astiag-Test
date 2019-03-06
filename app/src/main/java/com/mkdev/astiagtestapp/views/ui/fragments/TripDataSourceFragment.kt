package com.mkdev.astiagtestapp.views.ui.fragments

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.models.Address
import com.mkdev.astiagtestapp.models.LocationModel
import com.mkdev.astiagtestapp.utils.CustomToast
import com.mkdev.astiagtestapp.utils.gone
import com.mkdev.astiagtestapp.utils.iomain
import com.mkdev.astiagtestapp.utils.visible
import com.mkdev.astiagtestapp.viewModels.TripDataFragmentViewModel
import com.mkdev.astiagtestapp.viewModels.ViewModelFactory
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_trip_data_source.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


class TripDataSourceFragment : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v) {
            tvAccept -> {
                changeUiToDest()
            }
        }
    }

    private val viewDestroyCompositeDisposable = CompositeDisposable()
    private val destroyCompositeDisposable = CompositeDisposable()

    private lateinit var viewModel: TripDataFragmentViewModel
    private lateinit var currentAddress: LocationModel

    companion object {
        var tripDataActionsPublisher: PublishProcessor<Pair<MainFragment.ActionType, Any?>> = PublishProcessor.create()

        fun newInstance() = TripDataSourceFragment()
    }

    override fun initBeforeView() {
        with(context!!.applicationContext as App) {
            viewModel = ViewModelProviders.of(this@TripDataSourceFragment, ViewModelFactory(this))
                    .get(TripDataFragmentViewModel::class.java)
        }
    }

    override fun getContentViewId(): Int = R.layout.fragment_trip_data_source

    override fun initViews(rootView: View) {
        tripDataActionsPublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it.first) {
                        MainFragment.ActionType.SHOW_LOCATION_DATA -> {
                            getCurrentLocationData(it.second as LatLng)
                        }
                    }
                }.addTo(viewDestroyCompositeDisposable)

        tvAccept.setOnClickListener(this)
    }

    private fun getCurrentLocationData(loc: LatLng) {
        viewModel.getCurrentLocationData(loc)
                .delay(500, TimeUnit.MILLISECONDS)
                .iomain()
                .doOnSubscribe {
                    tvOriginTitle.text = getString(R.string.loading_address)
                    tvOriginSubTitle.gone()
                }
                .doAfterTerminate {
                    tvOriginSubTitle.visible()
                }
                .subscribe({
                    currentAddress = it
                    tvOriginTitle.text = it.getMainAddress()
                    tvOriginSubTitle.text = it.getSubAddress()
                }, {
                    Timber.e(it)
                    CustomToast.makeText(context!!, getString(R.string.server_error), CustomToast.ERROR)
                }).addTo(viewDestroyCompositeDisposable)
    }

    private fun changeUiToDest() {
        MainFragment.mainActionsPublisher.onNext(Pair(MainFragment.ActionType.ACCEPT_SOURCE, currentAddress))
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
