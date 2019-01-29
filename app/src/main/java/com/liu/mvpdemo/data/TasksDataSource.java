package com.liu.mvpdemo.data;

import com.liu.mvpdemo.bean.Task;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * 数据源接口.采用工厂模式，本地获取和远程获取数据的类都实现此接口（本Demo只采用本地获取的方式）
 *
 * @author liuxuhui
 * @date 2019/1/28
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

