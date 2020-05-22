package com.tool.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ScheduleViewHolder extends RecyclerView.ViewHolder {

    public TextView scheduleTitle;
    public TextView scheduleStart;
    public TextView scheduleEnd;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);
        scheduleTitle = itemView.findViewById(R.id.schedule_title);
        scheduleStart = itemView.findViewById(R.id.schedule_start);
        scheduleEnd = itemView.findViewById(R.id.schedule_end);

    }




}


