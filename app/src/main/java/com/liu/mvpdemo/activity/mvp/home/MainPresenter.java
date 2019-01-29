package com.liu.mvpdemo.activity.mvp.home;

import android.util.Log;

import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
import com.liu.mvpdemo.data.TasksDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author liuxuhui
 * @date 2019/1/28
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
                .compose(RxUtil.applyObservableThread())
                .flatMap(new Function<List<Task>, ObservableSource<Task>>() {
                    @Override
                    public ObservableSource<Task> apply(List<Task> tasks) throws Exception {
                        return Observable.fromIterable(tasks);
                    }
                })
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
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
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(List<Task> tasks) {
                        processTasks(tasks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.showLoadingTasksError();
                        mMainView.setLoadingIndicator(false);
                    }
                });
    }

    private void processTasks(List<Task> tasks){
        mMainView.setLoadingIndicator(false);
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

