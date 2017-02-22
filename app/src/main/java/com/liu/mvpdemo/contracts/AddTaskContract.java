package com.liu.mvpdemo.contracts;

import com.liu.mvpdemo.base.BasePresenter;
import com.liu.mvpdemo.base.BaseView;

/**
 * 项目名称：MvpDemo
 * 类描述：添加任务的合同接口
 * 创建人：liuxuhui
 * 创建时间：2017/2/22 16:01
 * 修改人：liuxuhui
 * 修改时间：2017/2/22 16:01
 * 修改备注：
 */

public interface AddTaskContract {

    interface View extends BaseView<Presenet>{

        /**
         * 返回任务列表页
         */
        void toTasksList();

        /**
         * 保存时，值为空，提示
         */
        void showEmptyError();
    }

    interface Presenet extends BasePresenter{

        /**
         * 保存任务
         * @param title
         * @param content
         */
        void saveTask(String title, String content);
    }
}
