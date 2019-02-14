package com.liu.mvpdemo.activity.mvp.login;

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

    interface Presenter {
        /**
         * 用户校验
         * @param name
         * @param password
         */
        void confimUser(String name, String password);
    }
}
