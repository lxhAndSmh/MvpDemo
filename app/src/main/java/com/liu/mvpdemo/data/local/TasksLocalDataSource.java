package com.liu.mvpdemo.data.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.data.TasksDataSource;
import com.liu.mvpdemo.data.local.TasksPersistenceContract.TaskEntry;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private final BriteDatabase mDatabaseHelper;
    private Function<Cursor, Task> mTaskFunction;
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Tasks.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TasksPersistenceContract.TaskEntry.TABLE_NAME + " (" +
                    TasksPersistenceContract.TaskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                    " )";


    private TasksLocalDataSource(Context context) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        SupportSQLiteOpenHelper.Configuration configuration = SupportSQLiteOpenHelper.Configuration.builder(context.getApplicationContext())
                .name(DATABASE_NAME)
                .callback(new DbCallback(DATABASE_VERSION))
                .build();
        SupportSQLiteOpenHelper.Factory factory = new FrameworkSQLiteOpenHelperFactory();
        SupportSQLiteOpenHelper mOpenHelper = factory.create(configuration);
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mOpenHelper, Schedulers.io());
        mTaskFunction = this::getTask;
    }

    class DbCallback extends SupportSQLiteOpenHelper.Callback {

        DbCallback(int version) {
            super(version);
        }

        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            Log.i(ConstantValues.TAG, "[db] 创建数据库");
            db.beginTransaction();
            try{
                db.execSQL(SQL_CREATE_ENTRIES);
            }finally {
                db.endTransaction();
            }
//            db.execSQL(Cabinet.CREATE_TABLE);
//            db.execSQL(Cell.CREATE_TABLE);
//            db.execSQL(Parcel.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(ConstantValues.TAG,
                    "[db] 升级数据库，db = [" + db.getPath() + "], oldVersion = [" + oldVersion + "], currentVersion = [" + newVersion + "]");
        }
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
        List<Task> tasks = new ArrayList<>();
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

        mDatabaseHelper.insert(TaskEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);

    }

    @Override
    public void completeTask(@NonNull Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { task.getId() };

        mDatabaseHelper.update(TaskEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);

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

        mDatabaseHelper.update(TaskEntry.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);

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
        mDatabaseHelper.delete(TaskEntry.TABLE_NAME, "", "");

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { taskId };

        mDatabaseHelper.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

    }
}

