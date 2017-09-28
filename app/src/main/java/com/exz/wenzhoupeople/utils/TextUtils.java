package com.exz.wenzhoupeople.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by pc on 2017/8/31.
 */

public class TextUtils {

    public static void setTextChanesColor(Context c, TextView v, int color, String allText, int start, int end){
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder builder = new SpannableStringBuilder(allText);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        v.setText(builder);
    }
}
