package com.liu.mvpdemo.activity.mvp.login;

/**
 * @author liuxuhui
 * @date 2019/4/10
 */
public interface NetworCallBack {

    /** 成功*/
    void onSuccess(Object data);

    /** 失败*/
    void onFail(int code, String msg);

}
