package com.ssin.todolist.ui.main.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ssin.todolist.R;
import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.TaskHeader;
import com.ssin.todolist.model.Taskable;
import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.receiver.OverdueReceiver;
import com.ssin.todolist.ui.ediddialog.EditTextDialog;
import com.ssin.todolist.ui.main.adapter.TaskAdapter;
import com.ssin.todolist.ui.main.module.DaggerMainComponent;
import com.ssin.todolist.ui.main.module.MainModule;
import com.ssin.todolist.ui.main.presenter.MainPresenter;
import com.ssin.todolist.ui.newtask.view.NewTaskActiivity;
import com.ssin.todolist.ui.splash.view.SplashActivity;
import com.ssin.todolist.util.AlarmUtil;
import com.ssin.todolist.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainView, AdapterView.OnItemClickListener, OverdueReceiver.Callback, TaskAdapter.OnTaskDoneListener {
    @BindView(R.id.list_view) ListView list;
    @BindView(R.id.fab_new_task)
    FloatingActionButton newTaskFab;
    @BindView(R.id.toolbar)  Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    //nav header
    TextView tvUserDisplayName;
    TextView tvUserEmail;

    @BindString(R.string.today) String today;
    @BindString(R.string.tomorrow) String tomorrow;
    @BindString(R.string.tags)
    String tagsStr;
    @BindString(R.string.add_new_tag)
    String newTagStr;
    @BindString(R.string.my_tasks)
    String myTasksStr;
    @BindString(R.string.snooze)
    String snoozeStr;

    @Inject
    MainPresenter presenter;

    private int order = 0;
    private int tagMenuOrder = 0;
    private SubMenu menuTags;
    private TaskAdapter adapter;
    private OverdueReceiver overdueReceiver;
    private boolean filtered;
    private String lastChildIdFromNotification;

    public static final String ACTION_LOGIN = "action_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        View headerLayout = navigationView.getHeaderView(0);
        ButterKnife.bind(headerLayout);

        tvUserDisplayName = (TextView) headerLayout.findViewById(R.id.text_view_user_displayname);
        tvUserEmail = (TextView) headerLayout.findViewById(R.id.text_view_user_email);

        DaggerMainComponent.builder()
                .firebaseModule(new FirebaseModule())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

        overdueReceiver = new OverdueReceiver(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(myTasksStr);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        final SubMenu filters = navigationView.getMenu().addSubMenu(0, 0, 999, "Filters");
        filters.add(0, 0, order++, today).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);
                presenter.onGetTasksByDate(DateTimeUtil.getDateNow());
                getSupportActionBar().setTitle(menuItem.getTitle());
                return true;
            }
        }).setIcon(getResources().getDrawable(R.drawable.calendar_today));
        filters.add(0, 0, order++, tomorrow).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);
                presenter.onGetTasksByDate(DateTimeUtil.getDateTomorow());
                getSupportActionBar().setTitle(menuItem.getTitle());
                return true;
            }
        }).setIcon(getResources().getDrawable(R.drawable.calendar_today));

        menuTags = navigationView.getMenu().addSubMenu(0, 0, 998, tagsStr);

        presenter.onAllTaskFetch();
        presenter.onAllTagsFetch();

        setTaskList(new ArrayList<Taskable>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OverdueReceiver.ACTION_ADAPTER_UPDATE);
        registerReceiver(overdueReceiver,intentFilter);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(overdueReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getUserInfo();
    }

    @OnClick(R.id.fab_new_task)
    public void newTask(){
        Intent intent = new Intent(this, NewTaskActiivity.class);
        intent.putExtra(NewTaskActiivity.EXTRA_MODE,NewTaskActiivity.EXTRA_MODE_CREATE);

        String[] tagsArray = new String[menuTags.size()];
        for (int i = 0; i < menuTags.size() - 1; i++) {
            MenuItem mi = menuTags.getItem(i);
            tagsArray[i] = mi.getTitle().toString();
        }
        intent.putExtra(NewTaskActiivity.EXTRA_TAGS_TO_CREATE, tagsArray);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("INFO", "onActivityResult: requestCode: " + requestCode);
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

                Map<String, Tag> tags = new HashMap<>();
                for(int i = 0; i < tags_extra.length; i++){
                    Tag tag = new Tag();
                    tag.setName(tags_extra[i]);
                    tags.put(tags_extra[i], tag);
                    Log.d("Tags",String.valueOf(getResources().getColor(R.color.black)));
                }
                task3.setTags(tags);
                task3.setDone(done);
                task3.setRepeatFreq(repeat);
                task3.setRemindFreq(remind);

                if(data.getStringExtra(NewTaskActiivity.EXTRA_MODE).equals(NewTaskActiivity.EXTRA_MODE_CREATE)) {
                    task3.setTaskId((int)System.currentTimeMillis());
                    presenter.onNewTaskAdd(task3);
                } else {
                    AlarmUtil.cancelAlarm(task3,getApplicationContext());
                    task3.setTaskId((int)System.currentTimeMillis());
                    AlarmUtil.setAlarm(task3,getApplicationContext());
                    presenter.onTaskUpdate(task3);
                }

                adapter.notifyDataSetChanged();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_all_tasks) {
            presenter.onAllTaskFetch();
            filtered = false;
            getSupportActionBar().setTitle(myTasksStr);

        } else if (id == R.id.nav_logout) {
            presenter.logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTaskList(List<Taskable> tasks) {
        adapter = new TaskAdapter(this, tasks, this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void setTagsList(List<Tag> list) {
        menuTags.clear();
        tagMenuOrder = 0;
        for (Tag t : list) {
            menuTags.add(0, 0, tagMenuOrder++, t.getName())
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    drawer.closeDrawer(GravityCompat.START);
                    presenter.onGetTasksByTag(menuItem.getTitle().toString());
                    getSupportActionBar().setTitle(menuItem.getTitle());
                    filtered = true;
                    return true;
                }
                    }).setIcon(getResources().getDrawable(R.drawable.ic_label_black_48dp));
        }
        menuTags.add(0, 0, 1000, newTagStr).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);

                EditTextDialog dialog = new EditTextDialog(MainActivity.this, new EditTextDialog.OnTextSetListener() {
                    @Override
                    public void onTextSet(String text) {
                        menuTags.add(0, 0, order++, text);
                        presenter.onNewTagAdd(text);
                    }
                });
                dialog.getAlertDialog().setTitle(newTagStr);
                dialog.getAlertDialog().show();
                return true;
            }
        }).setIcon(getResources().getDrawable(R.drawable.ic_add_black_48dp));
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

        Map<String, Tag> checkedTags = task.getTags();

        int j = 0;
        String[] arr = new String[checkedTags.size()];
        for (Map.Entry<String, Tag> tag : checkedTags.entrySet()) {
            arr[j] = tag.getValue().getName();
            j++;
        }

        data.putExtra(NewTaskActiivity.EXTRA_TAGS,arr);
        data.putExtra(NewTaskActiivity.EXTRA_MODE,NewTaskActiivity.EXTRA_MODE_EDIT);
        data.putExtra(NewTaskActiivity.EXTRA_ITEM_POS,i);

        String[] tagsArray = new String[menuTags.size()];
        for (int k = 0; k < menuTags.size() - 1; k++) {
            MenuItem mi = menuTags.getItem(k);
            tagsArray[k] = mi.getTitle().toString();
            Log.d("INFO", "Menu entry: " + menuTags.getItem(k).getTitle());
        }

        data.putExtra(NewTaskActiivity.EXTRA_TAGS_TO_CREATE, tagsArray);

        startActivityForResult(data,1);
    }

    @Override
    public void onOverdue() {
        adapter.notifyDataSetChanged();
        Log.d("Receiver", "Overdue!!!!!!!!!!");
    }

    @Override
    public void addNewItem(Task task) {
        if (adapter != null) {
            if (!filtered)
                adapter.add(task);
            AlarmUtil.setAlarm(task, getApplicationContext());
        }
    }

    @Override
    public void onTaskDone(Task task) {
        presenter.onTaskUpdate(task);
    }

    @Override
    public void navigateToSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setUserProfile(String email, String displayName) {
        if (displayName != null)
            tvUserDisplayName.setText(displayName);
        tvUserEmail.setText(email);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals("action_done")) {
            String childid = intent.getStringExtra(NewTaskActiivity.EXTRA_CHILD_ID);
            int notid = intent.getIntExtra("notification_id", -1);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(notid);
            Task t = adapter.getTaskById(childid);
            t.setDone(true);
            adapter.notifyDataSetChanged();
            presenter.onTaskUpdate(t);
        }

        if (intent.getAction().equals("action_snooze")) {
            String childid = intent.getStringExtra(NewTaskActiivity.EXTRA_CHILD_ID);
            lastChildIdFromNotification = childid;
            int notid = intent.getIntExtra("notification_id", -1);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(notid);

            registerForContextMenu(newTaskFab);
            openContextMenu(newTaskFab);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(snoozeStr);
        getMenuInflater().inflate(R.menu.menu_context_snooze, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_date_and_time) {
            handleSnoozeAction(lastChildIdFromNotification);
        }
        return true;
    }

    private void handleSnoozeAction(String childId) {
        final Task task = adapter.getTaskById(childId);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                task.setDate(DateTimeUtil.getDate(i, i1, i2));

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        task.setTime(DateTimeUtil.getTime(i, i1));
                        AlarmUtil.setAlarm(task, getApplicationContext());
                        adapter.notifyDataSetChanged();
                        presenter.onTaskUpdate(task);
                    }
                }, hour, minute, true);
                timePickerDialog.show();

            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void setAlarms() {
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equals(ACTION_LOGIN)) {
                if (adapter != null) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Task t = (Task) adapter.getItem(i);
                        AlarmUtil.setAlarm(t, getApplicationContext());
                    }
                }
            }
        }
    }

    @Override
    public void cancelAllAlarms() {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                Task t = (Task) adapter.getItem(i);
                AlarmUtil.cancelAlarm(t, getApplicationContext());
            }
        }
    }
}
