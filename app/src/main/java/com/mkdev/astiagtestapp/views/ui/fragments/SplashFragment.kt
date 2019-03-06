package com.mkdev.astiagtestapp.views.ui.fragments

import android.view.View
import androidx.navigation.Navigation
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.views.ui.base.BaseFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class SplashFragment : BaseFragment() {

    private val destroyCompositeDisposable = CompositeDisposable()

    override fun initBeforeView() {

    }

    override fun getContentViewId(): Int = R.layout.fragment_splash

    override fun initViews(rootView: View) {
        Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    Navigation.findNavController(view!!).navigate(R.id.action_splashFragment_to_mainFragment)
                }, {}).addTo(destroyCompositeDisposable)
    }

    override fun onDestroyView() {
        destroyCompositeDisposable.dispose()
        super.onDestroyView()
    }
}
