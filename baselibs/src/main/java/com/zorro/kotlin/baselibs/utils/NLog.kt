package com.zorro.kotlin.baselibs.utils

import android.util.Log

/**
 * @author Zorro
 * @date 2019/11/9
 * @desc 日志打印类
 */
object NLog {

    private var debug = true

    fun i(tag: String, content: String) {
        if (debug) {
            Log.i(tag, content)
        }
    }

    fun i(content: String) {
        i("Zorro", content)
    }

    fun v(tag: String, content: String) {
        if (debug) {
            Log.v(tag, content)
        }
    }

    fun v(content: String) {
        v("Zorro", content)
    }

    fun d(tag: String, content: String) {
        if (debug) {
            Log.d(tag, content)
        }
    }

    fun d(content: String) {
        d("Zorro", content)
    }

    fun w(tag: String, content: String) {
        if (debug) {
            Log.w(tag, content)
        }
    }

    fun w(content: String) {
        w("Zorro", content)
    }

    fun e(tag: String, content: String) {
        if (debug) {
            Log.e(tag, content)
        }
    }

    fun e(content: String) {
        e("Zorro", content)
    }

}