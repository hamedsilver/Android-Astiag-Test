package com.mkdev.astiagtestapp.views.ui.fragments

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.utils.AppBarType
import com.mkdev.astiagtestapp.utils.initToolbar
import com.mkdev.astiagtestapp.views.ui.activities.MainActivity
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(), View.OnClickListener {

    override fun initBeforeView() {

    }

    override fun getContentViewId(): Int = R.layout.fragment_main

    override fun initViews(rootView: View) {
        setHasOptionsMenu(true)
        rootView.toolbarMain.initToolbar(
            activity as MainActivity,
            getString(R.string.main_fragment_title), AppBarType.DRAWER
        )

        fabMain.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            fabMain -> {
                Snackbar.make(view, getString(R.string.main_snack_bar), Snackbar.LENGTH_LONG).apply {
                    setAction(getString(R.string.main_snack_bar_action)) { this.dismiss() }
                    setActionTextColor(ContextCompat.getColor(context, R.color.colorSnackBarActionText))
                }.show()
            }
        }
    }
}
