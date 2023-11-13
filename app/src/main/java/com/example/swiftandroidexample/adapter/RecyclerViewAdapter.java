package com.example.swiftandroidexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swiftandroidexample.R;
import com.example.swiftandroidexample.model.TaskModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<TaskModel> taskModels;
    private final OnTaskItemClickListner taskItemClickListner;

    public RecyclerViewAdapter(Context context, ArrayList<TaskModel> taskModels, OnTaskItemClickListner taskItemClickListner) {
        this.context = context;
        this.taskModels = taskModels;
        this.taskItemClickListner = taskItemClickListner;
    }

    public interface OnTaskItemClickListner {
        void onTaskItemClick(TaskModel taskModel);

        boolean onTaskItemLongClick(TaskModel taskModel);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(taskModels.get(position), taskItemClickListner);
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.titleTV);
        }

        public void bind(final TaskModel taskModel, final OnTaskItemClickListner taskItemClickListner) {
            titleTV.setText(taskModel.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskItemClickListner.onTaskItemClick(taskModel);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return taskItemClickListner.onTaskItemLongClick(taskModel);
                }
            });
        }
    }
}
