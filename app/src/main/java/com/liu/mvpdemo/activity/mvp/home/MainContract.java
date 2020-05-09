package com.liu.mvpdemo.activity.mvp.home;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {

        /**
         * 显示任务列表
         * @param tasks
         */
        void showTasks(List<Task> tasks);

        /**
         * 添加任务
         */
        void showAddTask();

        /**
         * 详情页
         * @param taskId
         */
        void showTaskDetailsUi(String taskId);

        /**
         * 显示加载任务失败的信息
         */
        void showLoadingTasksError();

        /**
         * 显示无数据时的UI
         */
        void showNoTasks();

        void showNoActivedTasks();

        void showNoCompletedTasks();

        /**
         * 显示过滤条件的PopUpMenu
         */
        void showFilteringPopUpMenu();

        /**
         * 显示加载进度圈
         */
        void setLoadingIndicator(final boolean active);

        /**
         * 显示过滤条件的标签
         */
        void showAllFilterLabel();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();
    }

    interface Presenter extends BasePresenter {

        /**
         * 是否更新数据
         * @param forceUpdate
         */
        void loadTasks(boolean forceUpdate);

        /**
         * 添加新的数据
         */
        void addNewTask();

        /**
         * 打开任务详情
         */
        void openTaskDetails(@NonNull Task requestTask);

        /**
         * 完成任务
         */
        void completeTask(@NonNull Task completeTask);

        /**
         * 有效的（未完成的任务）
         */
        void activateTask(@NonNull Task activeTask);

        /**
         * 清理完成的任务
         */
        void cleanCompletedTasks();

        /**
         * 设置过滤条件
         */
        void setFiltering(TasksFilterType filterType);

        TasksFilterType getFilterType();

        /**
         * 绑定服务，远程服务 AIDL、IPC
         */
        void bindAidlService();
    }

}
