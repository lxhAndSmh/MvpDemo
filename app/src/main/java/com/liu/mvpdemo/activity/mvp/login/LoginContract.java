package com.liu.mvpdemo.activity.mvp.login;


import io.reactivex.Observable;

/**
 * @author liuxuhui
 * @date 2019/2/11
 */
public interface LoginContract {

    interface View {

        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 登录失败
         */
        void loginFailed();

    }

    interface Model {
        /**
         * 模拟网路请求
         * @param name
         * @param password
         */
        Observable<String> uploadUserInfo(String name, String password);
        void uploadUserInfoByThread(String name, String password, NetworCallBack callBack);
    }

    interface Presenter {
        /**
         * 用户校验
         * @param name
         * @param password
         */
        void confirmUser(String name, String password);

        /**
         * 模拟网络请求登录
         * @param name
         * @param password
         */
        void loginByNetwork(String name, String password);
    }
}
