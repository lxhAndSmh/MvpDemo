package com.liu.mvpdemo.activity.mvp.taskdetail;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {

        void showTitle(String title);

        void showDescription(String description);

        void showCheckBox(boolean isCompleted);

        void toTaskLists();
    }

    interface Presenter extends BasePresenter {

        void setDeatil();

        void deleteTask();

        void setTaskId(String taskId);

        void setTaskCompleted(String taskId);

        void setTaskActived(String taskId);
    }
}