package com.ssin.todolist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ssin.todolist.service.NotificationRingtoneService;

public class StopRingtoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stopIntent = new Intent(context, NotificationRingtoneService.class);
        context.stopService(stopIntent);
    }
}
