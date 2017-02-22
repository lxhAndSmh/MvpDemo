package com.liu.mvpdemo.bean;


import android.text.TextUtils;

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

    private String mId;

    private String mTitle;

    private String mDescription;

    private boolean mCompleted;

    public Task(String mId, String mTitle, String mDescription, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    public Task(String mId, String mTitle, String mDescription) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }

    public Task(String mTitle, String mDescription) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public void setCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mTitle) &&
                TextUtils.isEmpty(mDescription);
    }

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
