package com.liu.mvpdemo.presenters;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
import com.liu.mvpdemo.contracts.MainContract;
import com.liu.mvpdemo.data.TasksDataSource;

import java.util.ArrayList;
import java.util.List;

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

        mTasksDataManager.getTasks(new TasksDataSource.LoadTaskCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<Task>();

                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETE_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }

                if (showLoadingUI) {
                    mMainView.setLoadingIndicator(false);
                }

                showFilterLabel();
                if (!tasksToShow.isEmpty()) {
                    mMainView.showTasks(tasksToShow);
                }else {
                    showEmptyMessage();
                }
            }

            @Override
            public void onDataNotAvailable() {
                mMainView.showLoadingTasksError();
            }
        });
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
