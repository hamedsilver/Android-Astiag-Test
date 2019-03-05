package com.mkdev.astiagtestapp.views.ui.fragments

import android.view.View
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment

class TripDataFragment : BaseFragment() {

    companion object {
        private var instance: TripDataFragment? = null

        fun newInstance(): TripDataFragment {
            if (instance == null) {
                instance = TripDataFragment()
            }
            return instance!!
        }
    }

    override fun initBeforeView() {

    }

    override fun getContentViewId(): Int = R.layout.fragment_trip_data

    override fun initViews(rootView: View) {

    }
}
