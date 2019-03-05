package com.mkdev.astiagtestapp.views.ui.base

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mkdev.astiagtestapp.Constants
import com.mkdev.astiagtestapp.utils.PermissionCallback
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    lateinit var permissionCallback: PermissionCallback
    private val permissionRequestCode = 3384

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

    fun requestPermission(permissions: Array<String>, permissionCallback: PermissionCallback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionCallback.onGranted(permissions)
            return
        }
        this.permissionCallback = permissionCallback
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionRequestCode) {

            var granted: Array<String> = arrayOf()
            var denied: Array<String> = arrayOf()
            var rationale: Array<String> = arrayOf()

            permissions.forEachIndexed { index, s ->
                when (grantResults[index]) {
                    PackageManager.PERMISSION_GRANTED -> granted = granted.plus(s)
                    PackageManager.PERMISSION_DENIED -> {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                            rationale = rationale.plus(s)
                        } else {
                            denied = denied.plus(s)
                        }
                    }
                }
            }

            if (granted.isNotEmpty())
                permissionCallback.onGranted(granted)

            if (rationale.isNotEmpty())
                permissionCallback.onShowRationale(rationale)

            if (denied.isNotEmpty())
                permissionCallback.onDenied(denied)
        }
    }
}