package com.liu.mvpdemo.activity.mvp.addtask;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;

/**
 * 添加任务的合同接口
 * @author liuxuhui
 * @date 2019/1/28
 */

public interface AddTaskContract {

    interface View extends BaseView<Presenet> {

        /**
         * 返回任务列表页
         */
        void toTasksList();

        /**
         * 保存时，值为空，提示
         */
        void showEmptyError();
    }

    interface Presenet extends BasePresenter {

        /**
         * 保存任务
         * @param title
         * @param content
         */
        void saveTask(String title, String content);
    }
}
