package com.zorro.kotlin.baselibs.utils

import java.util.*

/**
 * Created by Zorro on 2020/1/6.
 * 备注：生成随机颜色
 */
object ColorUtils {

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    fun getRandColor(): String {
        var r: String
        var g: String
        var b: String
        val random = Random()
        r = Integer.toHexString(random.nextInt(256)).toUpperCase(Locale.getDefault())
        g = Integer.toHexString(random.nextInt(256)).toUpperCase(Locale.getDefault())
        b = Integer.toHexString(random.nextInt(256)).toUpperCase(Locale.getDefault())
        r = if (r.length == 1) "0$r" else r
        g = if (g.length == 1) "0$g" else g
        b = if (b.length == 1) "0$b" else b
        return "#$r$g$b"
    }
}