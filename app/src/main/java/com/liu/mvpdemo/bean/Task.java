package com.liu.mvpdemo.bean;

/**
 * 项目名称：MvpDemo
 * 类描述：
 * 创建人：liuxuhui
 * 创建时间：2017/2/17 17:02
 * 修改人：liuxuhui
 * 修改时间：2017/2/17 17:02
 * 修改备注：
 */

public class Task {

    public String mId;

    public String mTitle;

    public String mDescription;

    public boolean mCompleted;

    @Override
    public String toString() {
        return "Task{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCompleted=" + mCompleted +
                '}';
    }
}
