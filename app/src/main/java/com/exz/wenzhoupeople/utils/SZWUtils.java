package com.exz.wenzhoupeople.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.szw.lib.myframework.observer.SmsContentObserver;

/**
 * Created by 史忠文
 * on 2017/8/3.
 */

public class SZWUtils {
    /**
     * @param phoneNum 电话号码
     * @return 有隐藏中间
     */
    public static String hideMidPhone(@NonNull String phoneNum) {

        if (TextUtils.isEmpty(phoneNum))
            return "暂无电话";
        else if (phoneNum.length() != 11)
            return phoneNum;
        else
            return phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length() - 4, phoneNum.length());
    }
    /**
     * 注册读取短信observer
     *
     * @param context  上下文
     * @param mHandler 监听
     * @return observer
     */
    public static SmsContentObserver registerSMS(Context context, Handler mHandler) {
        //注册内容观察者获取短信
        SmsContentObserver smsContentObserver = new SmsContentObserver(context, mHandler);
        // ”表“内容观察者 ，通过测试我发现只能监听此Uri -----> content://sms
        // 监听不到其他的Uri 比如说 content://sms/outbox
        Uri smsUri = Uri.parse("content://sms");
        context.getContentResolver().registerContentObserver(smsUri, true, smsContentObserver);
        return smsContentObserver;
    }
    /**
     * @param mContext 上下文
     * @param textView 返回验证码的textView
     * @return 验证码handler
     */
    public static Handler patternCode(Context mContext, TextView textView) {
        return new MyHandler(mContext, textView);
    }

    private static class MyHandler extends Handler {
        Context mContext;
        TextView textView;

        private MyHandler(Context mContext, TextView textView) {
            this.mContext = mContext;
            this.textView = textView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String outbox = (String) msg.obj;
//            edCode.setText(outbox);
//            Toast.makeText(mContext, outbox, Toast.LENGTH_SHORT).show();
            textView.setText(SZWUtils.patternCode(outbox,4));
        }
    }
    /**
     * 从短信字符窜提取验证码
     * @param body 短信内容
     * @param length  验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    public static String patternCode(String body, int length) {
        // 首先([a-zA-Z0-9]{length})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{length})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{length})后面不能有数字出现


//  获得数字字母组合
//    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

//  获得纯数字
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + length+ "})(?![0-9])");

        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }
}
