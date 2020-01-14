package com.zorro.kotlin.baselibs.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * Created by Zorro 2019/11/7.
 * 备注：复制内容到剪切板
 */
fun copyContent(context: Context, content: String): Boolean {
    return try {
        //获取剪贴板管理器
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", content)
        // 将ClipData内容放到系统剪贴板里。
        clipboard.primaryClip = mClipData
        true
    } catch (e: Exception) {
        false
    }
}