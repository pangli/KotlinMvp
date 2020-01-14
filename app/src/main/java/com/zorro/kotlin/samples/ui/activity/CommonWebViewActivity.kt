package com.zorro.kotlin.samples.ui.activity

import android.view.View
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.base.ZorroBaseActivity
import com.zorro.kotlin.samples.utils.ActivityHelper.DATA
import kotlinx.android.synthetic.main.include_toolbar_layout.*
import kotlinx.android.synthetic.main.web_view_layout.*


/**
 * Created by Zorro on 2019/11/20.
 * 备注：通用WebView
 */
class CommonWebViewActivity : ZorroBaseActivity() {

    private lateinit var url: String


    override fun attachLayoutRes(): Int = R.layout.activity_common_web_view_layout


    override fun initView() {
        whiteImmersionBar(toolbar)
    }

    override fun initData() {
        super.initData()
        url = intent.getStringExtra(DATA)
    }

    override fun bindListener() {
        iv_left.setOnClickListener(onClickListener)

    }

    override fun start() {
        initWebView()
    }


    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_left -> {
                finish()
            }
        }
    }

    private fun initWebView() {
        webView.run {
            setTittleListener {
                tv_title.text = it
            }
            addProgressBar()
            addJavascriptInterface(false)

        }
        webView.loadUrl(url)

    }

    override fun onDestroy() {
        webView.destroy()
        web_view_container.removeAllViews()
        super.onDestroy()
    }
}