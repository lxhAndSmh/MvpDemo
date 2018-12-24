package com.liu.mvpdemo.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.data.TasksDataSource;
import com.liu.mvpdemo.data.local.TasksPersistenceContract.TaskEntry;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 项目名称：MvpDemo
 * 类描述：本地数据处理
 * 创建人：liuxuhui
 * 创建时间：2017/2/20 12:49
 * 修改人：liuxuhui
 * 修改时间：2017/2/20 12:49
 * 修改备注：
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private final BriteDatabase mDatabaseHelper;

    private Func1<Cursor, Task> mTaskFunction;

    private TasksLocalDataSource(@NonNull Context context) {
        TasksDbHelper mDbHelper = new TasksDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mDbHelper, Schedulers.io());
        mTaskFunction = this::getTask;
    }

    @NonNull
    private Task getTask(@NonNull Cursor c) {
        String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
        String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
        String description =
                c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
        boolean completed =
                c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
        return new Task(title, description, itemId, completed);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context){
        if(INSTANCE == null){
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Task>> getTasks() {
        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), TaskEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(TaskEntry.TABLE_NAME, sql)
                .mapToList(mTaskFunction);
    }

    @Override
    public Observable<Task> getTask(@NonNull String taskId) {
        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };
        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
                TextUtils.join(",", projection), TaskEntry.TABLE_NAME, TaskEntry.COLUMN_NAME_ENTRY_ID);
        return mDatabaseHelper.createQuery(TaskEntry.TABLE_NAME, sql, taskId)
                .mapToOneOrDefault(mTaskFunction, null);

    }

    @Override
    public void saveTask(@NonNull Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        mDatabaseHelper.insert(TaskEntry.TABLE_NAME, values,SQLiteDatabase.CONFLICT_REPLACE);

    }

    @Override
    public void completeTask(@NonNull Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { task.getId() };

        mDatabaseHelper.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

    }

    @Override
    public void completeTask(@NonNull String taskId) {
    }

    @Override
    public void activeTask(@NonNull Task task) {

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { task.getId() };

        mDatabaseHelper.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

    }

    @Override
    public void activeTask(@NonNull String taskId) {
    }

    @Override
    public void clearCompletedTasks() {

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = { "1" };

        mDatabaseHelper.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

    }

    @Override
    public void deleteAllTasks() {
        mDatabaseHelper.delete(TaskEntry.TABLE_NAME, null, null);

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { taskId };

        mDatabaseHelper.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

    }
}
