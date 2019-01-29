package com.liu.mvpdemo.bean;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Strings;

import java.util.UUID;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */
public class Task {

    private String mId;

    private String mTitle;

    private String mDescription;

    private boolean mCompleted;

    public Task(String mTitle, String mDescription, String mId, boolean mCompleted) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mCompleted = mCompleted;
    }

    public Task(String mId, String mTitle, String mDescription) {
        this(mTitle, mDescription, mId, false);
    }

    public Task(String mTitle, String mDescription) {
        this(mTitle, mDescription, UUID.randomUUID().toString(), false);
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

    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
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
