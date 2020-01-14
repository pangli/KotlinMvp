package com.zorro.kotlin.baselibs.utils;

import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;


import com.zorro.kotlin.baselibs.base.BaseApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zorro on 2019/11/15 16:57
 * 备注：  SpannableString 处理关键字高亮变色
 */
public class SpanUtils {
    /**
     * 加tag背景并且关键词变色处理
     *
     * @param tagBgColor   tag边框颜色
     * @param tagTextColor tag文字颜色
     * @param textColor    高亮文字颜色
     * @param tagString    需要加圆形边框的string
     * @param content      需要对关键字高亮处理的string
     * @param patterStr    需要变色的关键词 或者 正则表达式
     * @return
     * @throws Exception
     */
    public static SpannableStringBuilder getKeyWordSpan(@ColorInt int tagBgColor, @ColorInt int tagTextColor, @ColorInt int textColor, String tagString, String content, String patterStr) throws Exception {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(tagString);
        TagSpan span = new TagSpan(Paint.Style.STROKE, tagTextColor, tagBgColor,
                DisplayUtils.INSTANCE.dp2px(BaseApplication.Companion.getInstance(), 12f),
                DisplayUtils.INSTANCE.dp2px(BaseApplication.Companion.getInstance(), 2f),
                DisplayUtils.INSTANCE.dp2px(BaseApplication.Companion.getInstance(), 5f),
                3, 3,
                DisplayUtils.INSTANCE.dp2px(BaseApplication.Companion.getInstance(), 1f),
                DisplayUtils.INSTANCE.dp2px(BaseApplication.Companion.getInstance(), 1f));
        spannableString.setSpan(span, 0, tagString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        SpannableString highlightSpannableString = new SpannableString(content);
        Pattern patten = Pattern.compile(patterStr, Pattern.CASE_INSENSITIVE);
        dealPattern(textColor, highlightSpannableString, patten, 0);
        builder.append(highlightSpannableString);
        return builder;
    }


    /**
     * 关键词变色处理
     *
     * @param str
     * @param patterStr 需要变色的关键词 或者 正则表达式
     * @return
     */
    public static SpannableString getKeyWordSpan(@ColorInt int color, String str, String patterStr) throws Exception {
        SpannableString spannableString = new SpannableString(str);
        Pattern patten = Pattern.compile(patterStr, Pattern.CASE_INSENSITIVE);
        dealPattern(color, spannableString, patten, 0);
        return spannableString;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则将内容变色
     *
     * @param color
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private static void dealPattern(@ColorInt int color, SpannableString spannableString, Pattern patten, int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            // 计算该内容的长度，也就是要替换的字符串的长度
            int end = matcher.start() + key.length();
            //设置前景色span
            spannableString.setSpan(new ForegroundColorSpan(color), matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (end < spannableString.length()) {
                // 如果整个字符串还未验证完，则继续。。
                dealPattern(color, spannableString, patten, end);
            }
            break;
        }
    }

}
