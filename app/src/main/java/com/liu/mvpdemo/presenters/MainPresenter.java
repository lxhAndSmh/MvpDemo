package com.liu.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
import com.liu.mvpdemo.contracts.MainContract;
import com.liu.mvpdemo.data.TasksDataSource;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private final TasksDataSource mTasksDataManager;

    private final MainContract.View mMainView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public MainPresenter(TasksDataSource mTasksDataManager, MainContract.View mMainView) {
        this.mTasksDataManager = checkNotNull(mTasksDataManager, "数据管理类不能为空");
        this.mMainView = checkNotNull(mMainView, "mMainView 不能为空");

        mMainView.setPresenter(this);
    }

    /**
     * 只处理本地数据，强制刷新功能暂时无效
     *
     * @param forceUpdate
     */
    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void addNewTask() {
        mMainView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestTask) {
        checkNotNull(requestTask, "requestTask cannot bu null");
        mMainView.showTaskDetailsUi(requestTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completeTask) {
        checkNotNull(completeTask, "completeTask cannot bu null");
        mTasksDataManager.completeTask(completeTask);
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot bu null");
        mTasksDataManager.activeTask(activeTask);
        loadTasks(false, false);
    }

    @Override
    public void cleanCompletedTasks() {
        mTasksDataManager.clearCompletedTasks();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TasksFilterType filterType) {
        mCurrentFiltering = filterType;
    }

    @Override
    public TasksFilterType getFilterType() {
        return mCurrentFiltering;
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mMainView.setLoadingIndicator(true);
        }

        mTasksDataManager
                .getTasks()
                .flatMap(new Func1<List<Task>, Observable<Task>>() {
                    @Override
                    public Observable<Task> call(List<Task> tasks) {
                        return Observable.from(tasks);
                    }
                })
                .filter(new Func1<Task, Boolean>() {
                    @Override
                    public Boolean call(Task task) {
                        switch (mCurrentFiltering) {
                            case ACTIVE_TASKS:
                                return task.isActive();
                            case COMPLETE_TASKS:
                                return task.isCompleted();
                            case ALL_TASKS:
                            default:
                                return true;
                        }
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Task>>() {
                    @Override
                    public void call(List<Task> tasks) {
                        processTasks(tasks);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mMainView.showLoadingTasksError();
                        mMainView.setLoadingIndicator(false);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mMainView.setLoadingIndicator(false);
                    }
                });
    }

    private void processTasks(List<Task> tasks){
        if(tasks.isEmpty()){
            showEmptyMessage();
        }else {
            mMainView.showTasks(tasks);
        }
        showFilterLabel();
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mMainView.showActiveFilterLabel();
                break;
            case COMPLETE_TASKS:
                mMainView.showCompletedFilterLabel();
                break;
            default:
                mMainView.showAllFilterLabel();
                break;
        }
    }

    private void showEmptyMessage() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mMainView.showNoActivedTasks();
                break;
            case COMPLETE_TASKS:
                mMainView.showNoCompletedTasks();
                break;
            default:
                mMainView.showNoTasks();
                break;
        }
    }
}
