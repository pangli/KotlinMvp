package com.zorro.kotlin.baselibs.utils

import android.text.TextUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.regex.Pattern

/**
 * Created by Zorro on 2019/9/4.
 * 备注：数字转换
 */
object NumberFormatUtil {
    /**
     * 转百分数
     */
    fun stringFormatPercentage(string: String): String {
        val num = NumberFormat.getPercentInstance()
        num.maximumFractionDigits = 2
        return num.format(string.toDouble())
    }

    /**
     * 转百分数
     */
    fun doubleFormatPercentage(double: Double): String {
        val num = NumberFormat.getPercentInstance()
        num.maximumFractionDigits = 2
        return num.format(double)
    }

    /**
     * 使用正则表达式判断该字符串是否为数字
     */
    fun canParseInt(str: String): Boolean {
        val pattern: Pattern = Pattern.compile("^[\\d]*$")
        return pattern.matcher(str).matches()
    }

    /**
     * 千分位加逗号分割
     */
    fun getNumFormat(number: String?): String {
        val df = DecimalFormat("#,###,###.##")
        return if (!TextUtils.isEmpty(number)) df.format(number?.toDouble()) else "0"
    }
}
