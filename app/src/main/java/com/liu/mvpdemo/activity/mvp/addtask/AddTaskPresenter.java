package com.liu.mvpdemo.activity.mvp.addtask;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.data.TasksDataSource;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */

public class AddTaskPresenter implements AddTaskContract.Presenet {

    private final TasksDataSource mDataManager;

    private final AddTaskContract.View mView;

    public AddTaskPresenter(TasksDataSource mDataManager, AddTaskContract.View mView) {
        this.mDataManager = mDataManager;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void saveTask(String title, String content) {
        Task task = new Task(title, content);
        if(task.isEmpty()){
            mView.showEmptyError();
        }else {
            mDataManager.saveTask(task);
            mView.toTasksList();
        }
    }

    @Override
    public void start() {

    }
}
