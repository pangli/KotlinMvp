package com.zorro.kotlin.baselibs.widget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import com.zorro.kotlin.baselibs.R
import kotlinx.android.synthetic.main.layout_loading_dialog.*

/**
 * @author Zorro
 * @date 2018/11/27
 * @desc
 */

class LoadingDialog : Dialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)

    /**
     * 水波
     */
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        loadingBar.setIndeterminateDrawable(Wave())
//        super.onWindowFocusChanged(hasFocus)
//    }

    companion object {

        fun initDialog(context: Context, cancelable: Boolean): LoadingDialog? {
            return initDialog(context, null, cancelable)
        }

        fun initDialog(
            context: Context,
            message: CharSequence?,
            cancelable: Boolean
        ): LoadingDialog? {
            val dialog = LoadingDialog(context, R.style.LoadingDialog)
            dialog.setContentView(R.layout.layout_loading_dialog)
            if (message.isNullOrBlank()) {
                dialog.tv_load_msg?.visibility = View.GONE
            } else {
                dialog.tv_load_msg?.visibility = View.VISIBLE
                dialog.tv_load_msg?.text = message
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(cancelable)
            dialog.window?.attributes?.gravity = Gravity.CENTER
            val lp = dialog.window?.attributes
            lp?.dimAmount = 0.2f
            dialog.window?.attributes = lp
            //dialog.show()
            return dialog
        }
    }

}