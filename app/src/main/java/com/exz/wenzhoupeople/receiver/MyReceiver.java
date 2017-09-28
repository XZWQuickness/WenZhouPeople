package com.exz.wenzhoupeople.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.exz.wenzhoupeople.R;
import com.exz.wenzhoupeople.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.com.szw.lib.myframework.utils.RxBus;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    public  static String s;
    public  static String title;


    @Override
    public void onReceive(Context context, Intent intent) {
        RxBus.get().register(this);

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            // 第二步：实例化通知栏构造器NotificationCompat.Builder：
            Intent msgIntent = new Intent("com.yunmo.mypushdemo.permission.JPUSH_MESSAGE");
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.mipmap.ic_launcher);

            // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
            mBuilder.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.FLAG_ONGOING_EVENT);
            RemoteViews rv = new RemoteViews(context.getPackageName(),
                    R.layout.customer_notitfication_layout);
            mBuilder.setContent(rv);// 设置自定义notification布局
            rv.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
            rv.setTextViewText(R.id.title, "温州人家");
            rv.setTextViewText(R.id.text, bundle.getString(JPushInterface.EXTRA_MESSAGE));

            mBuilder.setLargeIcon(BitmapFactory.decodeResource(
                    context.getResources(), R.mipmap.ic_launcher));// 设置下拉图标
            mBuilder.setAutoCancel(false);
            mBuilder.setTicker(bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Notification notify = mBuilder.build();// API


            try {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                Iterator<String> it = json.keys();

                while (it.hasNext()) {
                    String myKey = it.next().toString();
                    s = json.optString(myKey);
                }

            } catch (JSONException e) {
                Log.e(TAG, "Get message extra JSON error!");
            }

            title = bundle.getString(JPushInterface.EXTRA_MESSAGE);


            Intent it = new Intent(context, NotificationClickReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, notify.flags,
                    it, notify.flags);
            mNotificationManager.cancel(notify.flags);
            notify.flags = Notification.FLAG_AUTO_CANCEL;
            notify.contentIntent = pi;
            mNotificationManager.notify((int) (System.currentTimeMillis() / 100),
                    notify);
            context.sendBroadcast(msgIntent);
//			play(context);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
//			play(context);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

//            //打开自定义的Activity
            Intent intent1 = new Intent(
                    context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(intent1);


//            Intent intent1 = new Intent(
//                    context, Demo.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            intent1.putExtra("name", bundle.getString(JPushInterface.EXTRA_EXTRA));
//            context.startActivity(intent1);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..


        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
//	MediaPlayer mpMediaPlayer;
//	protected void play(final Context context){
//			if (mpMediaPlayer==null)
//			mpMediaPlayer= MediaPlayer.create(context,R.raw.pikaqiu);
//		if (mpMediaPlayer.isPlaying())
//			mpMediaPlayer.reset();
//			mpMediaPlayer.start();
//	}

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }


                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                        Log.e("哈哈", myKey + " - " + json.optString(myKey));
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}




