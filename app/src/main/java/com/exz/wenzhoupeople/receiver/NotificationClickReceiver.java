package com.exz.wenzhoupeople.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.exz.wenzhoupeople.activity.MainActivity;


public class NotificationClickReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{



		Intent intent1 = new Intent(context, MainActivity.class);

		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(intent1);

	}


}
