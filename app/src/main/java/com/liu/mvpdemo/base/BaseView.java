package com.liu.mvpdemo.base;

/**
 * 项目名称：MvpDemo
 * 类描述：View中的顶级接口
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 16:03
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 16:03
 * 修改备注：
 */

public interface BaseView<T> {

    void setPresenter(T presenter);
}
