package com.zorro.kotlin.baselibs.webview

import android.content.Context
import com.orhanobut.logger.Logger
import com.tencent.smtt.sdk.QbSdk

/**
 * Created by Zorro on 2019/12/27.
 * 备注：X5WebView初始化方法
 */
object X5WebViewUtils {
    //x5内核初始化接口,必须有文件权限
    fun initX5Environment(context: Context) {
        //非wifi情况下，主动下载x5内核
        try {
            QbSdk.setDownloadWithoutWifi(true)
            QbSdk.initX5Environment(context, object : QbSdk.PreInitCallback {

                override fun onViewInitFinished(isSuccess: Boolean) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Logger.d("x5內核初始化完成的回调 onViewInitFinished is $isSuccess")
                }

                override fun onCoreInitFinished() {
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}