package com.ssin.todolist.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ssin.todolist.model.Task;
import com.ssin.todolist.receiver.AlarmReceiver;
import com.ssin.todolist.ui.newtask.view.NewTaskActiivity;

/**
 * Created by SS-In on 2018-07-14.
 */

public class AlarmUtil {
    public static void setAlarm(Task task, Context context){
        if(task.getDateTimeMilis() > System.currentTimeMillis() || !task.isDone()) {
            task.setOverdue(false);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY, task.getRepeatFreq());
            intent.putExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY, task.getRemindFreq());
            intent.setAction(AlarmReceiver.ACTION_ALARM);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDateTimeMilis(), alarmIntent);
        } else {
            task.setOverdue(true);
        }
    }

    public static void cancelAlarm(Task task,Context context){
        task.setOverdue(true);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY, task.getRepeatFreq());
        intent.putExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY, task.getRemindFreq());
        intent.setAction(AlarmReceiver.ACTION_ALARM);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }
}
