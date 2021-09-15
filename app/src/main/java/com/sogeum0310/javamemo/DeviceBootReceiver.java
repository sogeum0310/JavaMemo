package com.sogeum0310.javamemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class DeviceBootReceiver extends BroadcastReceiver {
    private static final String TAG = DeviceBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alramIntent  = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,alramIntent,0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm", Context.MODE_PRIVATE);
        long milis = sharedPreferences.getLong("nextNotifytime", Calendar.getInstance().getTimeInMillis());

        Calendar currentc = Calendar.getInstance();
//        Calendar

    }
}
