package com.liu.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
import com.liu.mvpdemo.contracts.MainContract;
import com.liu.mvpdemo.data.TasksDataManager;

/**
 * 项目名称：MvpDemo
 * 类描述：MVP中的P,它是关联M与V的纽带
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 18:43
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 18:43
 * 修改备注：
 */

public class MainPresenter implements MainContract.Presenter {

    private final TasksDataManager mTasksDataManager;

    private final MainContract.View mMainView;

    public MainPresenter(TasksDataManager mTasksDataManager, MainContract.View mMainView) {
        this.mTasksDataManager = mTasksDataManager;
        this.mMainView = mMainView;
    }


    @Override
    public void loadTasks(boolean forceUpdate) {

    }

    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestTask) {

    }

    @Override
    public void completeTask(@NonNull Task completeTask) {

    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

    }

    @Override
    public void cleanCompletedTasks() {

    }

    @Override
    public void setFiltering(TasksFilterType filterType) {

    }

    @Override
    public TasksFilterType getFilterType() {
        return null;
    }

    @Override
    public void start() {

    }
}
