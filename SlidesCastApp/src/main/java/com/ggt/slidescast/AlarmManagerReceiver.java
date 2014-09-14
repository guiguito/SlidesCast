package com.ggt.slidescast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast receiver used to perform a refresh regularly in background.
 *
 * @author guiguito
 */
public class AlarmManagerReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        RefreshService_.intent(context).start();
    }

    /**
     * Set alarm.
     *
     * @param context
     */
    public static void setAlarm(Context context, int numberOfDayInterval) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60 * 24 * numberOfDayInterval, pi); // Millisec * Second * Minute * hours * day
    }

    /**
     * Cancel alarm.
     *
     * @param context
     */
    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
