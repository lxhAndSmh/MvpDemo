package com.liu.mvpdemo.contracts;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;

import java.util.List;

/**
 * 项目名称：MvpDemo
 * 类描述：Contract是一个合同类（契约），里面有两个内部接口，分别是集成View和Presenter基类（它们的顶级接口）的V和P；
 *        定义V和P的时候直接实现内部接口就可以了，有什么方法，可以在这里简洁明了的看到
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 16:21
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 16:21
 * 修改备注：
 */

public interface MainContract {

    interface View extends BaseView<Presenter>{

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
         * 显示任务被标记完成
         */
        void showTaskMarkedComplete();

        /**
         * 显示有效的任务
         */
        void showTaskMarkedActive();

        /**
         * 显示清理已完成任务后的数据
         */
        void showCompletedTasksCleared();

        /**
         * 显示加载任务失败的信息
         */
        void showLoadingTasksError();

        /**
         * 显示无数据时的UI
         */
        void showNoTasks();

        /**
         * 显示过滤条件的标签
         */
        void showAllFilterLabel();

        /**
         * 显示保存成功的信息
         */
        void showSuccessfullySavedMessage();

        /**
         * 是否是有效的
         */
        boolean isActive();

        /**
         * 显示过滤条件的PopUpMenu
         */
        void showFilteringPopUpMenu();


    }

    interface Presenter extends BasePresenter{

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
    }

}
