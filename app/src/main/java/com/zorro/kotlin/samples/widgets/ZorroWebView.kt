package com.zorro.kotlin.samples.widgets
import android.content.Context

import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.widget.ImageView

import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.orhanobut.logger.Logger
import com.zorro.kotlin.baselibs.imageloader.GlideApp
import com.zorro.kotlin.baselibs.webview.AndroidJavaScript
import com.zorro.kotlin.baselibs.webview.X5WebView
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.network.getToken
import com.zorro.kotlin.samples.popup.ImageViewPopup
import java.io.File

/**
 * Created by Zorro on 2019/11/18.
 * 备注：WebView封装
 */
class ZorroWebView : X5WebView {

    private var popup: ImageViewPopup? = null
    private var basePopup: BasePopupView? = null
    var webViewContentHeight: Float = 0f

    constructor(arg0: Context) : super(arg0)

    constructor(arg0: Context, arg1: AttributeSet) : super(arg0, arg1)

    fun addJavascriptInterface(isLogin: Boolean) {
        bindJavascriptInterface(MyAndroidJavaScript(isLogin), "android")
    }

    /**
     * H5调用Android接口
     */
    private inner class MyAndroidJavaScript constructor(internal var isLogin: Boolean) :
        AndroidJavaScript() {
        @JavascriptInterface
        fun getUserToken(): String {
            Logger.d("getUserToken我被H5调用了")
            val token = getToken()
            if (isLogin && token.isBlank()) {
                //登录
            } else {
                return token
            }
            return token
        }

        @JavascriptInterface
        fun backUserTokenStatus() {
            Logger.d("Token失效我被H5调用了")
        }



        @JavascriptInterface
        fun openLargerImage(path: String) {
            Logger.d("查看大图被H5调用了")
            showImagePopup(path)
        }

        @JavascriptInterface
        fun getContentHeight(height: String) {
            Logger.d("获取内容高度被H5调用了$height")
            webViewContentHeight = height.toFloat()
        }
    }

    private fun showImagePopup(path: String) {
        post {
            popup = ImageViewPopup(context)
            popup?.setSingleSrcView(null, path)
            popup?.isShowSaveButton(false)
            popup?.setXPopupImageLoader(object : XPopupImageLoader {
                override fun loadImage(position: Int, uri: Any, imageView: ImageView) {
                    GlideApp.with(imageView).load(uri)
                        .placeholder(R.mipmap.default_img)
                        .error(R.mipmap.default_img)
                        .into(imageView)
                }

                override fun getImageFile(context: Context, uri: Any): File {
                    return GlideApp.with(context).downloadOnly().load(uri).submit().get()
                }

            })
            basePopup = XPopup.Builder(context)
                .asCustom(popup)
                .show()
        }
    }



    override fun destroy() {
        popup?.dismiss()
        basePopup?.dismiss()
        popup = null
        basePopup = null
        super.destroy()
    }
}
