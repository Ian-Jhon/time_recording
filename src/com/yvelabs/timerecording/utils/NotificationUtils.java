package com.yvelabs.timerecording.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


public class NotificationUtils {
	
	public Notification buildNotification (Context context, Class returnClass, Map<String, Object> extras, int smallIcon, String title, String ContentText) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(smallIcon)
		        .setContentTitle(title)
		        .setContentText(ContentText);
		Intent resultIntent = new Intent(context, returnClass);
		Bundle bundle = new Bundle();
		
		for (Map.Entry<String, Object> entrySet: extras.entrySet()) {
			if (entrySet.getValue() instanceof Parcelable) {
				bundle.putParcelable(entrySet.getKey(), (Parcelable) entrySet.getValue());
			} else if (entrySet.getValue() instanceof List) {
				bundle.putParcelableArrayList(entrySet.getKey(), (ArrayList<? extends Parcelable>) entrySet.getValue());
			} else if (entrySet.getValue() instanceof String) {
				bundle.putString(entrySet.getKey(), (String) entrySet.getValue());
			} else if (entrySet.getValue() instanceof Integer) {
				bundle.putInt(entrySet.getKey(), (Integer) entrySet.getValue());
			}
		}
		resultIntent.putExtras(bundle);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(returnClass);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		
		return mBuilder.build();
	}
	
	
	/**
	 * notify a notification
	 * @param context
	 * @param returnClass
	 * @param smallIcon
	 * @param title
	 * @param ContentText
	 */
	public void notifyNotification (Context context, Class returnClass, Map<String, Object> extras, int smallIcon, String title, String ContentText, int notificationId) {
		
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(notificationId, buildNotification(context, returnClass, extras, smallIcon, title, ContentText));
	}
	
	/**
	 * remove notification by id
	 * @param notificationId
	 */
	public void removeNotification (Context context, int notificationId) {
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(notificationId);
	}
	
	/**
	 * remove all notification
	 */
	public void removeAllNotification (Context context) {
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}
}
