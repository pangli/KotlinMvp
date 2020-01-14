package com.zorro.kotlin.samples.popup

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.NonNull
import android.webkit.JavascriptInterface
import com.lxj.xpopup.core.CenterPopupView
import com.orhanobut.logger.Logger
import com.zorro.kotlin.baselibs.webview.AndroidJavaScript
import com.zorro.kotlin.baselibs.webview.WebViewController
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.constant.Constant
import kotlinx.android.synthetic.main.popup_man_machine_verification.view.*
import org.json.JSONObject


/**
 * Created by Zorro on 2019/12/24.
 * 备注：人机验证中间弹窗
 */
@SuppressLint("ViewConstructor")
class ManMachineVerificationPopupView(@NonNull context: Context, private var onVerificationListener: OnVerificationListener?) :
    CenterPopupView(context) {
    private lateinit var webViewController: WebViewController
    private val urlString: String =
        "https://v.vaptcha.com/app/android.html?vid=${Constant.VID}&scene=1&lang=zh-CN&offline_server=https://www.vaptchadowntime.com/dometime"

    override fun getImplLayoutId(): Int = R.layout.popup_man_machine_verification

    override fun initPopupContent() {
        super.initPopupContent()
        webViewController = WebViewController(context, webView_container)
    }

    /**
     * 宽度充满屏幕
     */
    override fun getMaxWidth(): Int = 0

    override fun onCreate() {
        super.onCreate()
        initWebView()
    }

    private fun initWebView() {
        webViewController.addJavascriptInterface(MyAndroidJavaScript(), "vaptchaInterface")
        webViewController.loadUrl(urlString)
    }

    override fun dismiss() {
        onVerificationListener = null
        webViewController.onDestroy()
        super.dismiss()
    }

    /**
     * H5调用Android接口
     */
    private inner class MyAndroidJavaScript : AndroidJavaScript() {
        @JavascriptInterface
        fun signal(json: String) {
            Logger.d("人机验证结果$json")
            if (onVerificationListener != null && !json.isNullOrBlank()) {
                val jsonObject = JSONObject(json)
                val signal = jsonObject.optString("signal")
                if ("pass" == signal) {
                    webView_container.post {
                        onVerificationListener?.onResultToKen(jsonObject.optString("data"))
                        dismiss()
                    }
                } else if ("cancel" == signal) {
                    webView_container.post {
                        dismiss()
                    }
                }
            }
        }
    }
}