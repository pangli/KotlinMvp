package com.zorro.kotlin.samples.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.ui.activity.*
import com.zorro.kotlin.samples.ui.im.activity.ConversationActivity
import com.zorro.kotlin.samples.ui.login.activity.LoginActivity


object ActivityHelper {
    const val DATA = "data"


    /**
     * 跳转登录
     * @param context 必传不为空
     */
    fun jumpToLoginActivity(activity: Activity) {
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.overridePendingTransition(R.anim.famous_hall_enter, R.anim.famous_hall_none)
    }

    /**
     * 跳转主页
     * @param context 必传不为空
     */
    fun jumpToMainActivity(activity: Activity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


    /**
     * 跳转通用webView
     * @param context 必传不为空
     */
    fun jumpToCommonWebViewActivity(context: Context, url: String) {
        val intent = Intent(context, CommonWebViewActivity::class.java)
        intent.putExtra(DATA, url)
        context.startActivity(intent)
    }

    /**
     * 跳转文件浏览
     * @param context 必传不为空
     * @param bundle 携带数据
     */
    fun jumpToFileViewerActivity(context: Context, bundle: Bundle) {
        val intent = Intent(context, FileViewerActivity::class.java)
        intent.putExtra(DATA, bundle)
        context.startActivity(intent)
    }

    /**
     * WebSocket页
     * @param context 必传不为空
     */
    fun jumpToWebSocketActivity(context: Context) {
        context.startActivity(Intent(context, WebSocketActivity::class.java))
    }

    /**
     * 聊天室页
     * @param context 必传不为空
     */
    fun jumpToConversationActivity(context: Context) {
        context.startActivity(Intent(context, ConversationActivity::class.java))
    }

    /**
     * 跳转Pdf文件浏览
     * @param context 必传不为空
     * @param bundle 携带数据
     */
    fun jumpToPDFViewActivity(context: Context, bundle: Bundle) {
        val intent = Intent(context, PDFViewActivity::class.java)
        intent.putExtra(DATA, bundle)
        context.startActivity(intent)
    }
}
