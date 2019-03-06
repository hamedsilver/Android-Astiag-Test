package com.mkdev.astiagtestapp.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mkdev.astiagtestapp.R

class CustomToast {

    companion object {
        const val SUCCESS = 1
        const val WARNING = 2
        const val ERROR = 3
        const val INFO = 4
        const val DEFAULT = 5
        private var textColor: Int = 0

        fun makeText(context: Context, msg: String, type: Int) {
            val layout = LinearLayout(context)
            val tv = TextView(context)

            when (type) {
                SUCCESS -> setBackgroundAndTextColor(context, layout, R.color.toast_success, R.color.md_white_1000)
                ERROR -> setBackgroundAndTextColor(context, layout, R.color.toast_error, R.color.md_white_1000)
                INFO -> setBackgroundAndTextColor(context, layout, R.color.toast_info, R.color.md_white_1000)
                DEFAULT -> setBackgroundAndTextColor(context, layout, R.color.toast_default, R.color.md_black_1000)
                WARNING -> setBackgroundAndTextColor(context, layout, R.color.toast_warning, R.color.md_black_1000)
            }

            layout.setPadding(dpToPixel(context, 10), dpToPixel(context, 5), dpToPixel(context, 10),
                    dpToPixel(context, 5)
            )
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(dpToPixel(context, 10), dpToPixel(context, 10), dpToPixel(context, 10),
                    dpToPixel(context, 10)
            )
            layout.layoutParams = params

            // set the TextView properties like color, size etc
            tv.setTextColor(textColor)
            tv.textSize = 14f

            tv.gravity = Gravity.CENTER
            //tv.typeface = TypeFaceUtils.getTypeFace(context)

            // set the text you want to show in  Toast
            tv.text = msg

            layout.addView(tv)

            val toast =
                    Toast(context) //context is object of Context write "this" if you are an Activity
            // Set The layout as Toast View
            toast.view = layout
            toast.duration = Toast.LENGTH_LONG

            // Position you toast here toast position is 50 dp from bottom you can give any integral value
            toast.setGravity(Gravity.BOTTOM, 0, dpToPixel(context, 40))
            toast.show()
        }

        private fun setBackgroundAndTextColor(
                context: Context,
                layout: LinearLayout,
                toast_color: Int,
                text_color: Int
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                layout.background = drawRoundRect(ContextCompat.getColor(context, toast_color))
            } else {
                layout.setBackgroundColor(toast_color)
            }
            textColor = ContextCompat.getColor(context, text_color)
        }

        private fun drawRoundRect(backgroundColor: Int): GradientDrawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadius = 10f
            shape.setColor(backgroundColor)
            return shape
        }

        private fun dpToPixel(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
        }
    }

}
