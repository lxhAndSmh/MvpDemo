package com.liu.mvpdemo.constant;

/**
 * 项目名称：MvpDemo
 * 类描述：过滤条件
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 17:32
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 17:32
 * 修改备注：
 */

public enum TasksFilterType {
    /**
     * 不过滤
     */
    ALL_TASKS,

    /**
     * 过滤有效的任务
     */
    ACTIVE_TASKS,

    /**
     * 过滤已完成的任务
     */
    COMPLETE_TASKS
}
