package com.liu.mvpdemo.data;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.bean.Task;

import java.util.List;

/**
 * 项目名称：MvpDemo
 * 类描述：数据源接口.采用工厂模式，本地获取和远程获取数据的类都实现此接口（本Demo只采用本地获取的方式）
 * 创建人：liuxuhui
 * 创建时间：2017/2/20 11:30
 * 修改人：liuxuhui
 * 修改时间：2017/2/20 11:30
 * 修改备注：
 */

public interface TasksDataSource {

    interface LoadTaskCallback{

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback{

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    /**
     * 获取任务列表
     * @param callback
     */
    void getTasks(@NonNull LoadTaskCallback callback);

    /**
     * 获取对应id的任务
     * @param taskId
     * @param callback
     */
    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activeTask(@NonNull Task task);

    void activeTask(@NonNull String taskId);

    void clearCompletedTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);

}
