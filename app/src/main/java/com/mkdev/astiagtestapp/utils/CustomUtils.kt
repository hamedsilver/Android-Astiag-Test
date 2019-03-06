package com.mkdev.astiagtestapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.mkdev.astiagtestapp.R
import com.mkdev.astiagtestapp.views.ui.activities.MainActivity
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.regex.Pattern

fun isNetworkConnected(context: Context): Boolean {
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (manager != null) {
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable) {
            return true
        }
    }
    return false
}

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

fun usernameValidator(string: String) = Pattern.compile("^[a-z0-9_-]{4,15}$").matcher(string).matches()

fun phoneNumberValidator(string: String) = string.matches(Regex("^(0?9|989)[0-9]{9}$"))

private var lastClick = 0L
private val THRESHOLD = 2000
fun doubleClickExit(): Boolean {
    val now = System.currentTimeMillis()
    val b = now - lastClick < THRESHOLD
    lastClick = now
    return b
}

fun hideSoftInput(activity: Activity) {
    var view = activity.currentFocus
    if (view == null) view = View(activity)
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showSoftInput(edit: EditText, context: Context) {
    edit.isFocusable = true
    edit.isFocusableInTouchMode = true
    edit.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(edit, 0)
}

fun toggleSoftInput(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Snackbar.customMake(@NonNull view: View, @NonNull text: CharSequence, actionText: CharSequence, duration: Int = Snackbar.LENGTH_LONG, listener: View.OnClickListener): Snackbar {

    Snackbar.make(view, text, duration).setAction(actionText) {
        listener.onClick(view)
        this.dismiss()
    }.setActionTextColor(ContextCompat.getColor(context, R.color.md_yellow_500)).show()

    return this
}


fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

fun GlideRequest<Drawable>.rounded(radius: Int, margin: Int = 3): GlideRequest<Drawable> = this.apply(RequestOptions().transform(RoundedCornersTransformation(radius, margin)))

fun GlideRequest<Bitmap>.roundedBitmap(radius: Int, margin: Int = 3): GlideRequest<Bitmap> = this.apply(RequestOptions().transform(RoundedCornersTransformation(radius, margin)))


fun Toolbar.initToolbar(activity: MainActivity, toolbarTitle: String, appBarType: AppBarType = AppBarType.NONE) {

    activity.setSupportActionBar(this)
    activity.supportActionBar?.apply {
        title = SpannableString(toolbarTitle).apply {
            setSpan(AbsoluteSizeSpan(dpToPx(16)), 0, toolbarTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // set size
            //setSpan(ForegroundColorSpan(Color.RED), 0, toolbarTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // set color
        }

        when (appBarType) {
            AppBarType.DRAWER -> {
                val toggle = ActionBarDrawerToggle(activity, activity.drawerLayout, this@initToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
                activity.drawerLayout.addDrawerListener(toggle)
                toggle.syncState()
            }
            AppBarType.BACK -> {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setDisplayShowTitleEnabled(true)

                setNavigationOnClickListener {
                    Navigation.findNavController(this@initToolbar).popBackStack()
                }
            }
            else -> {
                Timber.d("AppBarType is not valid!")
            }
        }
    }
}

fun View.changeState(type: ViewType, @DrawableRes resId: Int) {
    when (type) {
        ViewType.ENABLE -> {
            isClickable = true
            setBackgroundResource(resId)
        }
        ViewType.DISABLE -> {
            isClickable = false
            setBackgroundResource(resId)
        }
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

enum class AppBarType {
    DRAWER, BACK, NONE
}

enum class ViewType {
    ENABLE, DISABLE
}