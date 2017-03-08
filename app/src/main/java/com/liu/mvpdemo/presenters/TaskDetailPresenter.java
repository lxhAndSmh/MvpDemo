package com.liu.mvpdemo.presenters;

import com.liu.mvpdemo.contracts.TaskDetailContract;
import com.liu.mvpdemo.data.TasksDataManager;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称：MvpDemo
 * 类描述：任务详情的Presenter
 * 创建人：liuxuhui
 * 创建时间：2017/2/23 18:22
 * 修改人：liuxuhui
 * 修改时间：2017/2/23 18:22
 * 修改备注：
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TasksDataManager tasksDataManager;
    private final TaskDetailContract.View mView;
    private String taskId;

    public TaskDetailPresenter(TasksDataManager tasksDataManager, TaskDetailContract.View mView) {
        this.tasksDataManager = tasksDataManager;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void setDeatil() {
        tasksDataManager.getTask(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(task -> {
                    mView.showTitle(task.getTitle());
                    mView.showDescription(task.getDescription());
                    mView.showCheckBox(task.isCompleted());
                });
    }

    @Override
    public void deleteTask() {
        tasksDataManager.deleteTask(taskId);
        mView.toTaskLists();
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void setTaskCompleted(String taskId) {
        tasksDataManager.completeTask(taskId);
    }

    @Override
    public void setTaskActived(String taskId) {
        tasksDataManager.activeTask(taskId);
    }

    @Override
    public void start() {

    }
}
