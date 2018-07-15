package com.ssin.todolist.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ssin.todolist.R;
import com.ssin.todolist.service.NotificationRingtoneService;
import com.ssin.todolist.ui.newtask.view.NewTaskActiivity;

/**
 * Created by SS-In on 2018-07-09.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "com.ssin.todolist.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_ALARM)) {
            Log.d("Receiver", "Alarm!!!!!!!!!!");
            Intent adapterIntent = new Intent();
            adapterIntent.setAction(OverdueReceiver.ACTION_ADAPTER_UPDATE);
            ;
            context.sendBroadcast(adapterIntent);

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent stopSvcIntent = new Intent(context, StopRingtoneReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 1, stopSvcIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(pi);
            builder.setAutoCancel(true);

            builder.setSmallIcon(R.drawable.ic_add_white_48dp);
            builder.setContentTitle("ToDoList");
            builder.setContentText("Alarm works!!");

            nm.notify(0, builder.build());

            int repeat = intent.getIntExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY, NewTaskActiivity.FREQ_NEVER);
            int remind = intent.getIntExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY, NewTaskActiivity.FREQ_RING_ONCE);
            Log.d("INFO", "Remind in receiver: " + remind);

            Intent svcIntent = new Intent(context, NotificationRingtoneService.class);
            context.stopService(svcIntent);
            switch (remind) {
                case NewTaskActiivity.FREQ_RING_ONCE:
                    svcIntent.putExtra(NotificationRingtoneService.EXTRA_RING, 1);
                    context.startService(svcIntent);
                    break;
                case NewTaskActiivity.FREQ_RING_FIVE_TIMES:
                    svcIntent.putExtra(NotificationRingtoneService.EXTRA_RING, 5);
                    context.startService(svcIntent);
                    break;
                case NewTaskActiivity.FREQ_RING_NONSTOP:
                    svcIntent.putExtra(NotificationRingtoneService.EXTRA_RING, Integer.MAX_VALUE);
                    context.startService(svcIntent);
                    break;
                default:
                    break;
            }
        }
    }
}
