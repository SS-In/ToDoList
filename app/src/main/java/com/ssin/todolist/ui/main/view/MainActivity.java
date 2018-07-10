package com.ssin.todolist.ui.main.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.ssin.todolist.R;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.TaskHeader;
import com.ssin.todolist.model.Taskable;
import com.ssin.todolist.receiver.AlarmReceiver;
import com.ssin.todolist.ui.main.adapter.TaskAdapter;
import com.ssin.todolist.ui.newtask.view.NewTaskActiivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    @BindView(R.id.list_view) ListView list;
    @BindView(R.id.fab_new_task)
    FloatingActionButton newTaskFab;

    @BindString(R.string.today) String today;
    @BindString(R.string.tomorrow) String tomorrow;

    private TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Taskable> l = new ArrayList<>();
        adapter = new TaskAdapter(this,l);
        list.setAdapter(adapter);
    }

    @OnClick(R.id.fab_new_task)
    public void newTask(){
        Intent intent = new Intent(this, NewTaskActiivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                String date = data.getStringExtra(NewTaskActiivity.EXTRA_DATE);
                String time = data.getStringExtra(NewTaskActiivity.EXTRA_TIME);
                String title = data.getStringExtra(NewTaskActiivity.EXTRA_TITLE);

                TaskHeader th3 = new TaskHeader(today,tomorrow);
                th3.setDate(date);
                th3.setTime(time);

                Task task3 = new Task();
                task3.setDate(date);
                task3.setTitle(title);
                task3.setTime(time);
                adapter.add(th3);
                adapter.add(task3);

                alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, task3.getDateTimeMilis(),alarmIntent);
                Log.d("INFO","Milis: " + task3.getDateTimeMilis() + " " + SystemClock.elapsedRealtime());
            }
        }
    }
}
