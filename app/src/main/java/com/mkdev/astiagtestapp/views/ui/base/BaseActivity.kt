package com.mkdev.astiagtestapp.views.ui.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mkdev.astiagtestapp.Constants
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Change locale settings in the app.
        val dm = resources.displayMetrics
        val conf = resources.configuration
        val locale = Locale(Constants.FA_LANG.toLowerCase())
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            conf.locale = locale
        else
            conf.setLocale(locale) // API 17+ only.
        resources.updateConfiguration(conf, dm)

        initBeforeView()
        setContentView(getContentViewId())
        initViews()
    }

    protected abstract fun initBeforeView()

    protected abstract fun getContentViewId(): Int

    protected abstract fun initViews()
}