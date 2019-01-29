package com.liu.mvpdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.adapter.clicklistener.MainRecyclerClickListener;
import com.liu.mvpdemo.adapter.viewholder.MainViewHolder;
import com.liu.mvpdemo.bean.Task;

import java.util.List;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private Context context;
    private List<Task> tasks;
    private MainRecyclerClickListener mClickListener;

    public MainRecyclerAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public void setItemClickListener(MainRecyclerClickListener mClickListener){
        this.mClickListener = mClickListener;
    }

    public void replaceData(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        final Task task = tasks.get(position);
        holder.checkBox.setChecked(task.isCompleted());
        if(task.isCompleted()){
            holder.linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.main_item_bg));
        }else {
            holder.linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.touch_feedback));
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task.isCompleted()){
                    mClickListener.onActivateTaskClick(task);
                }else {
                    mClickListener.onCompleteTaskClick(task);
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onOpenTaskDetialClick(task);
            }
        });

        holder.tvTitle.setText(task.getTitleForList());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

}
