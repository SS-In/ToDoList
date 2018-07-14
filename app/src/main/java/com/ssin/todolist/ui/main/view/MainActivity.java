package com.ssin.todolist.ui.main.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ssin.todolist.R;
import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.TaskHeader;
import com.ssin.todolist.model.Taskable;
import com.ssin.todolist.receiver.AlarmReceiver;

import com.ssin.todolist.receiver.OverdueReceiver;
import com.ssin.todolist.ui.ediddialog.EditTextDialog;
import com.ssin.todolist.ui.main.adapter.TaskAdapter;
import com.ssin.todolist.ui.newtask.view.NewTaskActiivity;
import com.ssin.todolist.util.AlarmUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView, AdapterView.OnItemClickListener, OverdueReceiver.Callback{
    private AlarmManager alarmManager;

    private OverdueReceiver overdueReceiver;
    @BindView(R.id.list_view) ListView list;
    @BindView(R.id.fab_new_task)
    FloatingActionButton newTaskFab;
    @BindView(R.id.toolbar)  Toolbar toolbar;
    @BindView(R.id.drawer_layout)    DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    @BindString(R.string.today) String today;
    @BindString(R.string.tomorrow) String tomorrow;

    private int order = 0;

    private TaskAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        overdueReceiver = new OverdueReceiver(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTagsList(null);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        final SubMenu filters = navigationView.getMenu().addSubMenu("Filters");
        filters.add(0,0,order++,"Today");
        filters.add(0,0,order++,"Tomorrow");
        filters.add(0,0,order++,"Uncategorized");
        filters.add(0,0,1000,"Add new filter").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);
                EditTextDialog dialog = new EditTextDialog(MainActivity.this, new EditTextDialog.OnTextSetListener() {
                    @Override
                    public void onTextSet(String text) {
                        Log.d("Dialog","Text from dialog: " + text);
                        filters.add(0,0,order++,text);
                    }
                });
                dialog.getAlertDialog().setTitle("Add new filter");
                dialog.getAlertDialog().show();
                return true;
            }
        });

        List<Taskable> l = new ArrayList<>();

        TaskHeader th = new TaskHeader("Today","Tomorrow");
        th.setDate("13-07-2018");
        l.add(th);
        for(int i = 0; i < 3; i++){
            Task task = new Task();

            task.setDate("11-07-2018");
            task.setTime("11:00");
            task.setTitle("Zadanie " + i);

            Tag t1 = new Tag();
            t1.setName("Work");
            t1.setBgColor(getResources().getColor(R.color.color_today));
            t1.setTxtColor(getResources().getColor(R.color.white));

            Tag t2 = new Tag();
            t2.setName("Home");
            t2.setBgColor(getResources().getColor(R.color.green_500));
            t2.setTxtColor(getResources().getColor(R.color.white));

            List<Tag> tt = new ArrayList<>();
            tt.add(t1);
            tt.add(t2);
            task.setTags(tt);

            l.add(task);
        }

        setTaskList(l);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OverdueReceiver.ACTION_ADAPTER_UPDATE);
        registerReceiver(overdueReceiver,intentFilter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(overdueReceiver);
    }

    @OnClick(R.id.fab_new_task)
    public void newTask(){
        Intent intent = new Intent(this, NewTaskActiivity.class);
        intent.putExtra(NewTaskActiivity.EXTRA_MODE,NewTaskActiivity.EXTRA_MODE_CREATE);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                String date = data.getStringExtra(NewTaskActiivity.EXTRA_DATE);
                String time = data.getStringExtra(NewTaskActiivity.EXTRA_TIME);
                String title = data.getStringExtra(NewTaskActiivity.EXTRA_TITLE);
                String[] tags_extra = (String[]) data.getStringArrayExtra(NewTaskActiivity.EXTRA_TAGS);
                boolean done = data.getBooleanExtra(NewTaskActiivity.EXTRA_DONE,false);
                int repeat = data.getIntExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY,NewTaskActiivity.FREQ_NEVER);
                int remind = data.getIntExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY,NewTaskActiivity.FREQ_RING_ONCE);

                TaskHeader th3 = new TaskHeader(today,tomorrow);
                th3.setDate(date);
                th3.setTime(time);

                Task task3 = null;

                if(data.getStringExtra(NewTaskActiivity.EXTRA_MODE).equals(NewTaskActiivity.EXTRA_MODE_CREATE)) {
                    task3 = new Task();
                } else {
                    int pos = data.getIntExtra(NewTaskActiivity.EXTRA_ITEM_POS,-1);
                    Log.d("INFO","Result pos: " + pos);
                    if(pos != -1) {
                        task3 = (Task) adapter.getItem(pos);
                    }
                }

                task3.setDate(date);
                task3.setTitle(title);
                task3.setTime(time);

                List<Tag> tags = new ArrayList<>();
                for(int i = 0; i < tags_extra.length; i++){
                    Tag tag = new Tag();
                    tag.setName(tags_extra[i]);
                    tags.add(tag);
                    Log.d("Tags",String.valueOf(getResources().getColor(R.color.black)));
                }
                task3.setTags(tags);
                task3.setDone(done);
                task3.setRepeatFreq(repeat);
                task3.setRemindFreq(remind);

                if(data.getStringExtra(NewTaskActiivity.EXTRA_MODE).equals(NewTaskActiivity.EXTRA_MODE_CREATE)) {
                    task3.setTaskId((int)System.currentTimeMillis());
                    adapter.add(th3);
                    adapter.add(task3);
                    AlarmUtil.setAlarm(task3,getApplicationContext());
                } else {
                    AlarmUtil.cancelAlarm(task3,getApplicationContext());
                    task3.setTaskId((int)System.currentTimeMillis());
                    AlarmUtil.setAlarm(task3,getApplicationContext());
                }

                adapter.notifyDataSetChanged();

                Log.d("INFO","Remind: " + remind);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTaskList(List<Taskable> tasks) {
        adapter = new TaskAdapter(this,tasks);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void setTagsList(List<Tag> list) {
        SubMenu tags = navigationView.getMenu().addSubMenu("Tags");
        tags.add("Home");
        tags.add("Work");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(!(adapter.getItem(i)  instanceof Task))
            return;

        Task task = (Task) adapter.getItem(i);

        Intent data = new Intent(this,NewTaskActiivity.class);
        data.putExtra(NewTaskActiivity.EXTRA_DATE,task.getDate());
        data.putExtra(NewTaskActiivity.EXTRA_TIME,task.getTime());
        data.putExtra(NewTaskActiivity.EXTRA_TITLE,task.getTitle());
        data.putExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY,task.getRemindFreq());
        data.putExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY,task.getRepeatFreq());
        data.putExtra(NewTaskActiivity.EXTRA_DONE,task.isDone());

        List<Tag> checkedTags = task.getTags();

        String[] arr = new String[checkedTags.size()];
        for(int j = 0; j < checkedTags.size(); j++)
            arr[j] = checkedTags.get(j).getName();
        data.putExtra(NewTaskActiivity.EXTRA_TAGS,arr);
        data.putExtra(NewTaskActiivity.EXTRA_MODE,NewTaskActiivity.EXTRA_MODE_EDIT);
        data.putExtra(NewTaskActiivity.EXTRA_ITEM_POS,i);

        startActivityForResult(data,1);
    }

    @Override
    public void onOverdue() {
        adapter.notifyDataSetChanged();
        Log.d("Receiver", "Overdue!!!!!!!!!!");
    }
}
