package com.tool.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tool.calendar.mInterface.OnItemClickListener;

import java.util.List;

/**
 *
 */
public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

    private List<Schedule> scheduleList;

    private OnItemClickListener onItemClickListener;

    ScheduleRecyclerViewAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;

    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schdule_card, parent, false);
        return new ScheduleViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder holder, final int position) {
        if (scheduleList != null && position < scheduleList.size()) {
            Schedule product = scheduleList.get(position);
            holder.scheduleTitle.setText(product.getTitle());
            holder.scheduleStart.setText(product.getStartDate());
            holder.scheduleEnd.setText(product.getEndDate());


            if (onItemClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView,pos);
                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }




}
