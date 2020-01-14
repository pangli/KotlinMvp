package com.zorro.kotlin.baselibs.widget

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.View
import com.zorro.kotlin.baselibs.R

import kotlinx.android.synthetic.main.dialog_layout.*

/**
 * Created by Zorro on 2020/1/2.
 * 备注：Dialog对话框
 */
class DialogUtils : AlertDialog {
    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    @JvmOverloads
    constructor(context: Context?, themeResId: Int = 0) : super(
        context,
        themeResId
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)
    }

    //////////////////////////////一下方法必须在show后调用////////////////////////////
    fun setTitleVisibility(visibility: Int): DialogUtils {
        tv_title.visibility = visibility
        return this
    }

    fun setTitleText(string: String): DialogUtils {
        tv_title.text = string
        return this
    }

    fun setContentText(string: String?): DialogUtils {
        tv_content.text = string
        return this
    }

    fun setLeftButtonVisibility(visibility: Int): DialogUtils {
        button_left.visibility = visibility
        if (visibility == View.GONE) {
            view2.visibility = View.GONE
        } else {
            view2.visibility = View.VISIBLE
        }
        return this
    }

    fun setLeftButtonTextColor(@ColorInt color: Int): DialogUtils {
        button_left.setTextColor(color)
        return this
    }

    fun setLeftButtonText(string: String): DialogUtils {
        button_left.text = string
        return this
    }

    fun setRightButtonTextColor(@ColorInt color: Int): DialogUtils {
        button_right.setTextColor(color)
        return this
    }

    fun setRightButtonText(string: String): DialogUtils {
        button_right.text = string
        return this
    }

    fun setNegativeClickListener(listener: View.OnClickListener): DialogUtils {
        button_left.setOnClickListener(listener)
        button_right.setOnClickListener(listener)
        return this
    }

}