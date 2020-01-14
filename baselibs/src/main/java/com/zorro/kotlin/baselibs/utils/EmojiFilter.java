package com.zorro.kotlin.baselibs.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.orhanobut.logger.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zorro on 2019/4/9 13:36
 * 备注： 表情过滤
 */
public class EmojiFilter implements InputFilter {

    private static final String TAG = "EmojiFilter";

    private Pattern emojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher emojiMatcher = emojiPattern.matcher(source);
        if (emojiMatcher.find()) {
            Logger.d("source: " + source.toString() + " is match.");
            return "";
        }
        return source;
    }

}

