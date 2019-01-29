package com.liu.mvpdemo.base;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */

public interface BaseView<T> {

    /***
     * 使View层可以获取presenter（在Presenter类的构造方法中，将自身赋值给View，则在View的子类中获取Presenter）
     * @param presenter
     */
    void setPresenter(T presenter);
}