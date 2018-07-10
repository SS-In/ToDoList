package com.ssin.todolist.ui.newtask.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import com.ssin.todolist.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActiivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    @BindString(R.string.title_new_task) String title;

    @BindArray(R.array.spinner_remind_frequency)
    String[] repeatArray;

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

    private long timeMilis;

    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_TIME = "time";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_REMIND_FREQUENCY = "freq";

    public static final int FREQ_NEVER = 0;
    public static final int FREQ_DAY = 1;
    public static final int FREQ_WEEK = 2;
    public static final int FREQ_MONTH = 3;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_actiivity);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,repeatArray);
        spinnerRepeat.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_task_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            Intent data = new Intent();
            data.putExtra(EXTRA_DATE,etDate.getText().toString());
            data.putExtra(EXTRA_TIME,etTime.getText().toString());
            data.putExtra(EXTRA_TITLE,etTitle.getText().toString());
            data.putExtra(EXTRA_REMIND_FREQUENCY,spinnerRepeat.getSelectedItemPosition());
            setResult(Activity.RESULT_OK,data);
            finish();
        }
        return true;
    }

    @OnClick(R.id.image_button_pick_time)
    public void showTimePickDialog(){
        if(timePickerDialog == null)
            timePickerDialog = new TimePickerDialog(this,this,0,0,true);
        timePickerDialog.show();
    }

    @OnClick(R.id.image_button_pick_date)
    public void showDatePickDialog(){
        if(datePickerDialog == null) {
            Calendar calendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        }
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

        etDate.setText(dateFormatter.format(calendar.getTime()));
        Log.d("INFO","Date: " + day + "-" + month + "-" + year);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minutes);

        etTime.setText(hourFormatter.format(calendar.getTime()));
        Log.d("INFO","Time: " + hour + ":" + minutes);
    }
}
