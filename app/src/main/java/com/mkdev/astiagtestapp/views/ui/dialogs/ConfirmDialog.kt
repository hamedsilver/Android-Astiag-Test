package com.mkdev.astiagtestapp.views.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import com.mkdev.astiagtestapp.R
import kotlinx.android.synthetic.main.dialog_confirm.*

class ConfirmDialog(private val mContext: Context,
                    private val title: String = mContext.getString(R.string.confirm),
                    private val content: String,
                    private val confirmListener: (ConfirmDialog) -> Unit,
                    cancelable: Boolean = true,
                    cancelListener: DialogInterface.OnCancelListener? = null) :
        Dialog(mContext, cancelable, cancelListener) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)
        window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)

        window?.setBackgroundDrawableResource(android.R.color.transparent)

        tvTitle.text = title
        tvContent.text = content
        btnConfirm.setOnClickListener {
            dismiss()
            confirmListener.invoke(this@ConfirmDialog)
        }
        btnDismiss.setOnClickListener {
            cancel()
        }
    }
}