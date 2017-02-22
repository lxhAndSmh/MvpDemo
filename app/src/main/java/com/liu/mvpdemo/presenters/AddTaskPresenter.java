package com.liu.mvpdemo.presenters;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.contracts.AddTaskContract;
import com.liu.mvpdemo.data.TasksDataSource;

/**
 * 项目名称：MvpDemo
 * 类描述：添加任务的Presenter
 * 创建人：liuxuhui
 * 创建时间：2017/2/22 16:18
 * 修改人：liuxuhui
 * 修改时间：2017/2/22 16:18
 * 修改备注：
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
