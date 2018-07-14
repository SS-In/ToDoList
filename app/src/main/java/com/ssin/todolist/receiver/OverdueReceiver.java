package com.ssin.todolist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SS-In on 2018-07-14.
 */

public class OverdueReceiver extends BroadcastReceiver {
    public static final String ACTION_ADAPTER_UPDATE = "com.ssin.todolist.adapterupdate";
    private Callback callback;

    public OverdueReceiver(Callback callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Overdue!!!!!!!!!!");
        if(intent.getAction().equals(ACTION_ADAPTER_UPDATE)) {
            if (callback != null) {
                callback.onOverdue();
            }
        }
    }

    public interface Callback {
        void onOverdue();
    }
}
