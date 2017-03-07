package com.liu.mvpdemo.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.liu.mvpdemo.bean.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 项目名称：MvpDemo
 * 类描述：MVP中的Model，数据控制器。用于操作远程数据和本地数据（本Demo中只操作本地数据）
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 18:40
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 18:40
 * 修改备注：
 */

public class TasksDataManager implements TasksDataSource{

    private static final String TAG = TasksDataManager.class.getSimpleName();

    private static TasksDataManager INSTANCE = null;

    private final TasksDataSource localDataSource;

    Map<String, Task> mCachedTasks;


    private TasksDataManager(@NonNull TasksDataSource localDataSource) {
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static TasksDataManager getInstance(TasksDataSource tasksLocalData){
        if(INSTANCE == null){
            INSTANCE = new TasksDataManager(tasksLocalData);
        }
        return INSTANCE;
    }

    public static void destoryInstance(){
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTaskCallback callback) {

        checkNotNull(callback);

        if(mCachedTasks != null){
            callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        localDataSource.getTasks(new LoadTaskCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                callback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "获取的tasks数据为空");
                callback.onDataNotAvailable();
            }
        });

    }

    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        localDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "获取的task数据为空");
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        localDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        localDataSource.completeTask(task);

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
        localDataSource.activeTask(task);

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
        localDataSource.clearCompletedTasks();

        // Do in memory cache update to keep the app UI up to date
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
        localDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        localDataSource.deleteTask(checkNotNull(taskId));

        mCachedTasks.remove(taskId);
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
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
