package com.liu.mvpdemo.data;

import android.support.annotation.NonNull;

import com.liu.mvpdemo.bean.Task;

import java.util.List;

import rx.Observable;

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

    /**
     * 获取任务列表
     */
    Observable<List<Task>> getTasks();

    /**
     * 获取对应id的任务
     * @param taskId
     */
    Observable<Task> getTask(@NonNull String taskId);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activeTask(@NonNull Task task);

    void activeTask(@NonNull String taskId);

    void clearCompletedTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);

}
