package com.ssin.todolist.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ssin.todolist.R;
import com.ssin.todolist.service.NotificationRingtoneService;
import com.ssin.todolist.ui.main.view.MainActivity;
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

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pi);
            builder.setAutoCancel(false);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);

            int notid = (int) System.currentTimeMillis();

            Intent doneIntent = new Intent(context, MainActivity.class);
            doneIntent.setAction("action_done");
            doneIntent.putExtra(NewTaskActiivity.EXTRA_CHILD_ID, intent.getStringExtra(NewTaskActiivity.EXTRA_CHILD_ID));
            doneIntent.putExtra("notification_id", notid);
            doneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent donePending = PendingIntent.getActivity(context, 2, doneIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent snoozeIntent = new Intent(context, MainActivity.class);
            snoozeIntent.setAction("action_snooze");
            snoozeIntent.putExtra(NewTaskActiivity.EXTRA_CHILD_ID, intent.getStringExtra(NewTaskActiivity.EXTRA_CHILD_ID));
            snoozeIntent.putExtra("notification_id", notid);
            snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent snoozePending = PendingIntent.getActivity(context, 3, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);


            builder.addAction(new NotificationCompat.Action(R.drawable.ic_done_white_36dp, "Done!", donePending));
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_snooze_black_48dp, "Snooze", snoozePending));

            builder.setSmallIcon(R.drawable.ic_add_white_48dp);
            builder.setContentTitle(intent.getStringExtra(NewTaskActiivity.EXTRA_TASK_TITLE));


            nm.notify(notid, builder.build());

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
