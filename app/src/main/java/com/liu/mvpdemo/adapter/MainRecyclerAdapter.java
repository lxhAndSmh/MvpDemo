package com.liu.mvpdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.viewholder.MainViewHolder;

import java.util.List;

/**
 * 项目名称：MvpDemo
 * 类描述：
 * 创建人：liuxuhui
 * 创建时间：2017/2/20 18:45
 * 修改人：liuxuhui
 * 修改时间：2017/2/20 18:45
 * 修改备注：
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private Context context;
    private List<Task> tasks;

    public MainRecyclerAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, null);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
