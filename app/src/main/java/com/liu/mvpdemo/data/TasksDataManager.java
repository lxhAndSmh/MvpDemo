package com.liu.mvpdemo.data;

import com.liu.mvpdemo.bean.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * MVP中的Model，数据控制器。用于操作远程数据和本地数据（本Demo中只操作本地数据）
 * @author liuxuhui
 * @date 2019/1/28
 */

public class TasksDataManager implements TasksDataSource {

    private static TasksDataManager INSTANCE = null;


    Map<String, Task> mCachedTasks;


    private TasksDataManager() {
    }

    public static TasksDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksDataManager();
        }
        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Task>> getTasks() {

        if (mCachedTasks != null) {
            return Observable.fromArray(new ArrayList<>(mCachedTasks.values()));
        }else {
            return Observable.fromArray(new ArrayList<>());
        }
    }

    @Override
    public Observable<Task> getTask(@NonNull final String taskId) {
        checkNotNull(taskId);

        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            return Observable.just(cachedTask);
        }

        return getTask(taskId)
                .map(new Function<Task, Task>() {
                    @Override
                    public Task apply(Task task) throws Exception {
                        if(task == null){
                            throw  new NoSuchElementException("No task found wih taskId " + taskId);
                        }
                        return task;
                    }
                });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activeTask(@NonNull Task task) {
        checkNotNull(task);

        Task activeTask = new Task(task.getId(), task.getTitle(), task.getDescription());

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }

    @Override
    public void activeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activeTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void deleteAllTasks() {

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {

        mCachedTasks.remove(taskId);
    }

    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }

}
