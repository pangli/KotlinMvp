package com.zorro.kotlin.samples.widgets

import android.content.Context

import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupImageLoader
import com.orhanobut.logger.Logger
import com.zorro.kotlin.baselibs.imageloader.GlideApp
import com.zorro.kotlin.baselibs.webview.AndroidJavaScript
import com.zorro.kotlin.baselibs.webview.WebViewController
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.network.getToken
import com.zorro.kotlin.samples.popup.ImageViewPopup
import java.io.File

/**
 * Created by Zorro on 2019/11/18.
 * 备注：原生WebView封装
 */
class ZorroGoogleWebView(
    private val context: Context,
    private val webViewConsole: FrameLayout,
    private val onPageFinished: (() -> Unit)? = null
) :
    WebViewController(context, webViewConsole) {

    private var popup: ImageViewPopup? = null
    private var basePopup: BasePopupView? = null
    @Volatile
    var webViewContentHeight: Float = 0f

    fun addJavascriptInterface(isLogin: Boolean) {
        addJavascriptInterface(MyAndroidJavaScript(isLogin), "android")
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
        @Synchronized
        fun getContentHeight(height: String) {
            Logger.d("获取内容高度被H5调用了$height")
            //webview加载完成之后重新设置高度
            webViewContentHeight = height.toFloat()
            if (webView != null && webViewContentHeight > 0)
                webViewConsole.post {
                    val params = webView.layoutParams
                    val scale = context.resources.displayMetrics.density
                    params.height = (webViewContentHeight * scale + 0.5).toInt()
                    webView.layoutParams = params
                    onPageFinished?.invoke()
                }
        }
    }

    private fun showImagePopup(path: String) {
        webViewConsole.post {
            popup = ImageViewPopup(context)
            popup?.setSingleSrcView(null, path)
            popup?.isShowSaveButton(false)
            popup?.setXPopupImageLoader(object : XPopupImageLoader {
                override fun loadImage(position: Int, uri: Any, imageView: ImageView) {
                    //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
                    GlideApp.with(imageView).load(uri)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.default_img)
                                .override(Target.SIZE_ORIGINAL)
                        )
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



    override fun onDestroy() {
        popup?.dismiss()
        basePopup?.dismiss()
        popup = null
        basePopup = null
        super.onDestroy()
    }
}
