package com.liu.mvpdemo.activity.mvp.taskdetail;

import com.liu.mvpdemo.data.TasksDataManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author liuxuhui
 * @date 2019/1/28
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