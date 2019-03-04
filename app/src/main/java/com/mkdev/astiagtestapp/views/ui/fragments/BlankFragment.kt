package com.mkdev.astiagtestapp.views.ui.fragments

import android.view.View
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.utils.AppBarType
import com.mkdev.astiagtestapp.utils.initToolbar
import com.mkdev.astiagtestapp.views.ui.activities.MainActivity
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import kotlinx.android.synthetic.main.app_bar_main.view.*

class BlankFragment : BaseFragment() {

    override fun initBeforeView() {

    }

    override fun getContentViewId(): Int = R.layout.fragment_blank

    override fun initViews(rootView: View) {
        setHasOptionsMenu(true)
        rootView.toolbarMain.initToolbar(
            activity as MainActivity,
            getString(R.string.blank_fragment_title), AppBarType.BACK
        )
    }
}
