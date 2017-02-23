package com.liu.mvpdemo.contracts;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;

/**
 * 项目名称：MvpDemo
 * 类描述：任务详情页的合同接口
 * 创建人：liuxuhui
 * 创建时间：2017/2/23 18:15
 * 修改人：liuxuhui
 * 修改时间：2017/2/23 18:15
 * 修改备注：
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter>{

        void showTitle(String title);

        void showDescription(String description);

        void showCheckBox(boolean isCompleted);

        void toTaskLists();
    }

    interface Presenter extends BasePresenter{

        void setTeatil();

        void deleteTask();

        void setTaskId(String taskId);
    }
}
