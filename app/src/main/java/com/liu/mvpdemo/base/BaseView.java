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

    /***
     * 使View层可以获取presenter（在Presenter类的构造方法中，将自身赋值给View，则在View的子类中获取Presenter）
     * @param presenter
     */
    void setPresenter(T presenter);
}
