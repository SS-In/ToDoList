package com.ssin.todolist.ui.newtask.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import com.ssin.todolist.R;
import com.ssin.todolist.util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActiivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    @BindString(R.string.error_title)
    String errorTitle;

    @BindString(R.string.delete_task)
    String deleteItemStr;
    @BindString(R.string.cancel)
    String cancelStr;
    @BindString(R.string.ok)
    String okStr;

    @BindString(R.string.title_new_task)
    String title;

    @BindArray(R.array.repeat_frequency)
    String[] repeatArray;

    @BindArray(R.array.remind_frequency)
    String[] remindArray;

    @BindView(R.id.edit_text_task_title)
    EditText etTitle;

    @BindView(R.id.edit_text_due_date)
    EditText etDate;

    @BindView(R.id.edit_text_due_time)
    EditText etTime;

    @BindView(R.id.image_button_pick_date)
    ImageButton ibDate;

    @BindView(R.id.image_button_pick_time)
    ImageButton ibTime;

    @BindView(R.id.spinner_repeat)
    Spinner spinnerRepeat;

    @BindView(R.id.linear_layout_tags_chkbxs)
    LinearLayout layoutCheckBoxes;

    @BindView(R.id.spinner_remind)
    Spinner spinnerRemind;

    @BindView(R.id.check_box_done)
    CheckBox chbxDone;

    private HashMap<String, CheckBox> checkBoxList;

    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_REMIND_FREQUENCY = "remind_freq";
    public static final String EXTRA_REPEAT_FREQUENCY = "repeat_freq";
    public static final String EXTRA_DONE = "done";
    public static final String EXTRA_TAGS = "tags";
    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_ITEM_POS = "itempos";
    public static final String EXTRA_CHILD_ID = "child_id";

    public static final String EXTRA_MODE_EDIT = "edit";
    public static final String EXTRA_MODE_CREATE = "create";
    public static final String EXTRA_TAGS_TO_CREATE = "tagstocreate";
    public static final String EXTRA_TASK_TITLE = "task_title";
    public static final String EXTRA_DELETE = "delete";

    private int itemPos = -1;

    public static final int FREQ_NEVER = 0;
    public static final int FREQ_DAY = 1;
    public static final int FREQ_WEEK = 2;
    public static final int FREQ_MONTH = 3;

    public static final int FREQ_RING_ONCE = 0;
    public static final int FREQ_RING_FIVE_TIMES = 1;
    public static final int FREQ_RING_NONSTOP = 2;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public static int RESULT_CREATE_EDIT = 9999;
    public static int RESULT_DELETE = 9998;

    private List<String> checkedTags;

    private String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_actiivity);

        checkedTags = new ArrayList<>();
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, repeatArray);
        spinnerRepeat.setAdapter(adapter);

        SpinnerAdapter adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, remindArray);
        spinnerRemind.setAdapter(adapter1);

        chbxDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    etTitle.setPaintFlags(etTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                else
                    etTitle.setPaintFlags(etTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
        checkBoxList = new HashMap<>();

        currentMode = getIntent().getStringExtra(NewTaskActiivity.EXTRA_MODE);

        etTime.setText(DateTimeUtil.getTimeNow());
        etDate.setText(DateTimeUtil.getDateNow());
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickDialog();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDialog();
            }
        });
        extractDataFromIntent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getStringExtra(EXTRA_MODE) == null || !getIntent().getStringExtra(EXTRA_MODE).equals(EXTRA_MODE_EDIT)) {
            getMenuInflater().inflate(R.menu.new_task_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            if(TextUtils.isEmpty(etTitle.getText().toString())) {
                etTitle.setError(errorTitle);
                return true;
            }

            etTitle.setError(null);

            Intent data = new Intent();
            data.putExtra(EXTRA_DATE, etDate.getText().toString());
            data.putExtra(EXTRA_TIME, etTime.getText().toString());
            data.putExtra(EXTRA_TITLE, etTitle.getText().toString());
            data.putExtra(EXTRA_REMIND_FREQUENCY, spinnerRemind.getSelectedItemPosition());
            data.putExtra(EXTRA_REPEAT_FREQUENCY, spinnerRepeat.getSelectedItemPosition());
            data.putExtra(EXTRA_DONE, chbxDone.isChecked());
            data.putExtra(EXTRA_ITEM_POS, itemPos);

            data.putExtra(EXTRA_MODE, currentMode);

            String[] arr = new String[checkedTags.size()];
            for (int i = 0; i < checkedTags.size(); i++)
                arr[i] = checkedTags.get(i);
            data.putExtra(EXTRA_TAGS, arr);

            for (String s : checkedTags)
                Log.d("NewTaskAct", s);
            setResult(RESULT_CREATE_EDIT, data);
            finish();
        } else if (item.getItemId() == R.id.action_delete) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(deleteItemStr)
                    .setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(okStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent data = new Intent();
                            data.putExtra(EXTRA_ITEM_POS, itemPos);
                            setResult(RESULT_DELETE, data);
                            dialogInterface.dismiss();
                            finish();
                        }
                    }).create();
            dialog.show();
        }
        return true;
    }

    @OnClick(R.id.image_button_pick_time)
    public void showTimePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();
    }

    @OnClick(R.id.image_button_pick_date)
    public void showDatePickDialog() {
        if (datePickerDialog == null) {
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        etDate.setText(dateFormatter.format(calendar.getTime()));
        Log.d("INFO", "Date: " + day + "-" + month + "-" + year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);

        etTime.setText(hourFormatter.format(calendar.getTime()));
        Log.d("INFO", "Time: " + hour + ":" + minutes);
    }

    private void extractDataFromIntent() {
        Intent data = getIntent();
        if (data == null)
            return;

        String[] tags = data.getStringArrayExtra(EXTRA_TAGS_TO_CREATE);
        for (int i = 0; i < tags.length - 1; i++) {
            final CheckBox cb = new CheckBox(this);
            cb.setText(tags[i]);
            layoutCheckBoxes.addView(cb);
            checkBoxList.put(tags[i], cb);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b)
                        checkedTags.add(cb.getText().toString());
                    else
                        checkedTags.remove(cb.getText().toString());

                }
            });
        }

        if (data.getStringExtra(EXTRA_MODE) == null || !data.getStringExtra(EXTRA_MODE).equals(EXTRA_MODE_EDIT)) {
            return;
        }

        String date = data.getStringExtra(NewTaskActiivity.EXTRA_DATE);
        String time = data.getStringExtra(NewTaskActiivity.EXTRA_TIME);
        String title = data.getStringExtra(NewTaskActiivity.EXTRA_TITLE);
        String[] tags_extra = (String[]) data.getStringArrayExtra(NewTaskActiivity.EXTRA_TAGS);
        boolean done = data.getBooleanExtra(NewTaskActiivity.EXTRA_DONE, false);
        int repeat = data.getIntExtra(NewTaskActiivity.EXTRA_REPEAT_FREQUENCY, NewTaskActiivity.FREQ_NEVER);
        int remind = data.getIntExtra(NewTaskActiivity.EXTRA_REMIND_FREQUENCY, NewTaskActiivity.FREQ_RING_ONCE);
        itemPos = data.getIntExtra(NewTaskActiivity.EXTRA_ITEM_POS, 0);

        etDate.setText(date);
        etTime.setText(time);
        etTitle.setText(title);
        chbxDone.setChecked(done);
        spinnerRemind.setSelection(remind);
        spinnerRepeat.setSelection(repeat);

        checkedTags.clear();
        for (int i = 0; i < tags_extra.length; i++) {
            Log.d("INFO", "Tag extra: " + tags_extra[i]);
            CheckBox cb = checkBoxList.get(tags_extra[i]);
            cb.setChecked(true);
        }

        for (Map.Entry<String, CheckBox> e : checkBoxList.entrySet()) {
            Log.d("INFO", "Key: " + e.getKey());
        }
    }
}
