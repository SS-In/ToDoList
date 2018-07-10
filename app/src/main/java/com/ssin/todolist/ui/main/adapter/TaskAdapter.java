package com.ssin.todolist.ui.main.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ssin.todolist.R;
import com.ssin.todolist.model.Task;
import com.ssin.todolist.model.TaskHeader;
import com.ssin.todolist.model.Taskable;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SS-In on 2018-07-07.
 */

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Taskable> items;

    private static final int TYPE_DATE = 0;
    private static final int TYPE_TASK = 1;

    public TaskAdapter(Context context, List<Taskable> items){
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
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

        public TaskViewHolder(View v){
            ButterKnife.bind(this,v);
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
        TaskViewHolder taskViewHolder;
        DateViewHolder dateViewHolder;

        if(getItemViewType(i) == TYPE_TASK) {
            if (view == null ||  !(view.getTag() instanceof TaskViewHolder)) {
                view = inflater.inflate(R.layout.task_item, viewGroup, false);
                taskViewHolder = new TaskViewHolder(view);
             //   taskViewHolder.titleTv = (TextView)view.findViewById(R.id.text_view_task_title);
                view.setTag(taskViewHolder);
            } else {
                taskViewHolder = (TaskViewHolder)view.getTag();
            }

            Task t = (Task)getTask(i);
            taskViewHolder.titleTv.setText(t.getTitle());
            taskViewHolder.timeTv.setText(t.getTime());

            return view;
        } else {
            if (view == null || !(view.getTag() instanceof DateViewHolder)) {
                view = inflater.inflate(R.layout.task_section_item, viewGroup, false);
                dateViewHolder = new DateViewHolder(view);
              //  dateViewHolder.dateTv = (TextView)view.findViewById(R.id.text_view_section);
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
}
