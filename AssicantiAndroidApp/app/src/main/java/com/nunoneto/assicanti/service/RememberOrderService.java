package com.nunoneto.assicanti.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.entity.realm.DayMenu;
import com.nunoneto.assicanti.ui.DayMenuActivity;

public class RememberOrderService extends IntentService {

    public RememberOrderService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AlarmManager am =( AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), DayMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute

    }
}
