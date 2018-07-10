package com.ssin.todolist.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.ssin.todolist.R;

/**
 * Created by SS-In on 2018-07-09.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver","Alarm!!!!!!!!!!");
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.drawable.ic_add_white_48dp);
        builder.setContentTitle("ToDoList");
        builder.setContentText("Alarm works!!");

        nm.notify(0,builder.build());
    }
}
