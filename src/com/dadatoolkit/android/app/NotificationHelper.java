package com.dadatoolkit.android.app;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
public class NotificationHelper {

	private NotificationManager NM = null;

	private Context mContext = null;

	private static NotificationHelper mInstance = null;

	private NotificationHelper(Context context) {
		// TODO Auto-generated constructor stub
		NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mContext = context;
	}


	public static NotificationHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new NotificationHelper(context);
		}
		return mInstance;
	}
	
	
	
	public void showNotification(int tickerText, int contentTitle,
			int contentText, int id, int resId,boolean ongoing,PendingIntent pendingIntent) {
		Resources r = mContext.getResources();
		showNotification(r.getString(tickerText),r.getString(contentTitle),r.getString(contentText),id,resId,ongoing,pendingIntent);
	}

	@SuppressWarnings("deprecation")
	public void showNotification(String tickerText, String contentTitle,
			String contentText, int id, int resId,boolean ongoing,PendingIntent pendingIntent) {

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			Notification.Builder builder = new Notification.Builder(mContext);
			builder.setSmallIcon(resId);
			builder.setTicker(tickerText);
			builder.setContentTitle(contentTitle);
			builder.setContentText(contentText);
			builder.setOngoing(ongoing);
			builder.setContentIntent(pendingIntent);
			NM.notify(id, builder.build());
		}else{
			Notification notification = new Notification(resId, tickerText, System
					.currentTimeMillis());
			notification
			.setLatestEventInfo(mContext, contentTitle, contentText, pendingIntent);
			if(ongoing)
				notification.flags = Notification.FLAG_ONGOING_EVENT;
			NM.notify(id, notification);
		}


	}	
	
	
	public void cancelNotification(int id){
		NM.cancel(id);
	}

}
