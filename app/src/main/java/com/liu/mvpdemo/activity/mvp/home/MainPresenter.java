package com.liu.mvpdemo.activity.mvp.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.botpy.sourcecodedemo.aidl.AIDL_Service;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
import com.liu.mvpdemo.data.TasksDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author liuxuhui
 * @date 2019/1/28
 */

public class MainPresenter implements MainContract.Presenter {

    private final TasksDataSource mTasksDataManager;

    private final MainContract.View mMainView;
    private MainActivity context;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public MainPresenter(TasksDataSource mTasksDataManager, MainContract.View mMainView, MainActivity context) {
        this.mTasksDataManager = checkNotNull(mTasksDataManager, "数据管理类不能为空");
        this.mMainView = checkNotNull(mMainView, "mMainView 不能为空");
        this.context = context;
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
    public void bindAidlService() {
        System.out.println("--AIDL_SERVICE--" + "点击了绑定按钮");
        //通过 Intent 指定服务端的服务名称和所在的包，与远程Service进行绑定
        //参数与服务器端的action要一致
        Intent intent = new Intent("com.botpy.sourcecodedemo.aidl.AIDL_Service");
        //Android 5.0之后无法通过隐式 Intent 绑定远程 Service，需要通过 setPackage() 方法指定包名
        intent.setPackage("com.botpy.sourcecodedemo.service");
        //绑定服务
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mMainView.setLoadingIndicator(true);
        }

        mTasksDataManager
                .getTasks()
                .compose(RxUtil.applyObservableThread())
                .flatMap(new Function<List<Task>, ObservableSource<Task>>() {
                    @Override
                    public ObservableSource<Task> apply(List<Task> tasks) throws Exception {
                        return Observable.fromIterable(tasks);
                    }
                })
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {
                        switch (mCurrentFiltering) {
                            case ACTIVE_TASKS:
                                return task.isActive();
                            case COMPLETE_TASKS:
                                return task.isCompleted();
                            case ALL_TASKS:
                            default:
                                return true;
                        }
                    }
                })
                .toList()
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(List<Task> tasks) {
                        processTasks(tasks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainView.showLoadingTasksError();
                        mMainView.setLoadingIndicator(false);
                    }
                });
    }

    private void processTasks(List<Task> tasks){
        mMainView.setLoadingIndicator(false);
        if(tasks.isEmpty()){
            showEmptyMessage();
        }else {
            mMainView.showTasks(tasks);
        }
        showFilterLabel();
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

    private AIDL_Service mService;
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 在 Activity 与 Service 建立关联的时候调用
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("--AIDL_SERVICE--" + "ComponentName: " + name + "   IBinder: " + service.toString());
            mService = AIDL_Service.Stub.asInterface(service);
            try {
                mService.AIDL_service();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        /**
         * 在 Activity 与 Service 建立关联和解除关联的时候调用
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("--AIDL_SERVICE--" + "ComponentName: " + name);
        }
    };
}

