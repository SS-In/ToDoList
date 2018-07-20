package com.ssin.todolist.ui.main.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssin.todolist.R;
import com.ssin.todolist.model.Tag;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.TaskHeader;
import com.ssin.todolist.model.Taskable;
import com.ssin.todolist.util.AlarmUtil;
import com.ssin.todolist.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SS-In on 2018-07-07.
 */

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Taskable> items;
    private OnTaskDoneListener onTaskDoneListener;

    private String todayStr;
    private String yesterdayStr;
    private String tomorrowStr;
    private String[] daysOfWeekArr;
    private String finishedStr;

    private static final int TYPE_DATE = 0;
    private static final int TYPE_TASK = 1;

    public TaskAdapter(Context context, List<Taskable> items, OnTaskDoneListener onTaskDoneListener) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.onTaskDoneListener = onTaskDoneListener;

        this.todayStr = context.getResources().getString(R.string.today);
        this.yesterdayStr = context.getResources().getString(R.string.yesterday);
        this.tomorrowStr = context.getResources().getString(R.string.tomorrow);
        this.daysOfWeekArr = context.getResources().getStringArray(R.array.days_of_week);
        this.finishedStr = context.getResources().getString(R.string.finished);
    }

    public static class DateViewHolder{
        @BindView(R.id.text_view_section) TextView dateTv;

        public DateViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }

    public static class TaskViewHolder{
        @BindView(R.id.text_view_task_title) TextView titleTv;
        @BindView(R.id.text_view_time) TextView timeTv;
        @BindView(R.id.linear_layout_categories) LinearLayout llTags;
        @BindView(R.id.checkBox) CheckBox doneChkBx;
        List<TextView> tvTags;

        public TaskViewHolder(View v){
            ButterKnife.bind(this,v);
            tvTags = new ArrayList<>();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getTask(position).isSection() ? TYPE_DATE : TYPE_TASK;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final TaskViewHolder taskViewHolder;
        DateViewHolder dateViewHolder;

        if(getItemViewType(i) == TYPE_TASK) {
            final Task t = (Task)getTask(i);


            if (view == null ||  !(view.getTag() instanceof TaskViewHolder)) {
                view = inflater.inflate(R.layout.task_item, viewGroup, false);
                view.setVisibility(View.VISIBLE);
                taskViewHolder = new TaskViewHolder(view);
                Map<String, Tag> tags = t.getTags();
                addTextViews(tags,taskViewHolder);
                view.setTag(taskViewHolder);
            } else {
                taskViewHolder = (TaskViewHolder)view.getTag();
            }

            taskViewHolder.titleTv.setText(t.getTitle());

            taskViewHolder.timeTv.setText(DateTimeUtil.getDateTimeWithDayName(daysOfWeekArr, t.getDate(), t.getTime(), todayStr, tomorrowStr, yesterdayStr));

            if (t.getDateTimeMilis() < System.currentTimeMillis() && !t.isDone()) {
                taskViewHolder.timeTv.setTextColor(context.getResources().getColor(R.color.red_600));

            } else if (t.isDone()) {
                taskViewHolder.timeTv.setTextColor(context.getResources().getColor(R.color.grey_400));
            } else {
                taskViewHolder.timeTv.setTextColor(context.getResources().getColor(R.color.green_500));
            }
            taskViewHolder.doneChkBx.setChecked(t.isDone());
            crossText(taskViewHolder, t.isDone(), t);

            if (!t.isDone()) {
                taskViewHolder.timeTv.setText(DateTimeUtil.getDateTimeWithDayName(daysOfWeekArr, t.getDate(), t.getTime(), todayStr, tomorrowStr, yesterdayStr));
            } else {
                taskViewHolder.timeTv.setText(finishedStr + " " + DateTimeUtil.getDateTimeWithDayName(daysOfWeekArr, t.getDoneDate(), t.getDoneTime(), todayStr, tomorrowStr, yesterdayStr));
            }

            taskViewHolder.doneChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    crossText(taskViewHolder, b, t);
                    t.setDone(b);

                    if (compoundButton.isPressed()) {
                        t.setDoneDate(DateTimeUtil.getDateNow());
                        t.setDoneTime(DateTimeUtil.getTimeNow());
                    }

                    if (onTaskDoneListener != null)
                        onTaskDoneListener.onTaskDone(t);
                    notifyDataSetChanged();
                }
            });
            if(t.getTags() != null) {
                if(t.getTags().size() == taskViewHolder.tvTags.size()) {
                    int j = 0;
                    for (Map.Entry<String, Tag> tag : t.getTags().entrySet()) {
                        TextView tv = taskViewHolder.tvTags.get(j);
                        tv.setText(tag.getValue().getName());
                        j++;
                    }
                } else {
                    taskViewHolder.tvTags.clear();
                    taskViewHolder.llTags.removeAllViews();
                    addTextViews(t.getTags(),taskViewHolder);
                }
            }

            return view;
        } else {
            if (view == null || !(view.getTag() instanceof DateViewHolder)) {
                view = inflater.inflate(R.layout.task_section_item, viewGroup, false);
                dateViewHolder = new DateViewHolder(view);
                view.setTag(dateViewHolder);
            } else {
                dateViewHolder = (DateViewHolder) view.getTag();
            }

            TaskHeader t = (TaskHeader) getTask(i);
            dateViewHolder.dateTv.setText(t.toString());

            return view;
        }
    }

    private Taskable getTask(int position){
        return (Taskable)getItem(position);
    }

    public void add(Taskable taskable){
        items.add(taskable);
        notifyDataSetChanged();
    }

    private void addTextViews(Map<String, Tag> tags, TaskViewHolder taskViewHolder) {
        if(tags != null) {
            for (Map.Entry<String, Tag> tag : tags.entrySet()) {
                TextView tv = new TextView(context);
                float scale = context.getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (3 * scale + 0.5f);
                tv.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                tv.setBackgroundColor(tag.getValue().getBgColor());
                tv.setTextColor(tag.getValue().getTxtColor());
                tv.setText(tag.getValue().getName());
                taskViewHolder.tvTags.add(tv);
                taskViewHolder.llTags.addView(tv);
            }
        }
    }

    public interface OnTaskDoneListener {
        void onTaskDone(Task task);
    }

    public Task getTaskById(String parent) {
        for (int i = 0; i < items.size(); i++) {
            if (!(getItem(i) instanceof Task)) {
                continue;
            } else {
                Task t = (Task) getItem(i);
                if (t.getParent().equals(parent))
                    return t;
            }
        }
        return null;
    }

    private void crossText(TaskViewHolder taskViewHolder, boolean cross, Task t) {
        if (cross) {
            taskViewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.grey_400));
            taskViewHolder.titleTv.setPaintFlags(taskViewHolder.titleTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            AlarmUtil.cancelAlarm(t, context);
        } else {
            taskViewHolder.titleTv.setTextColor(context.getResources().getColor(R.color.black));
            taskViewHolder.titleTv.setPaintFlags(taskViewHolder.titleTv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            AlarmUtil.setAlarm(t, context);

        }
    }
}
