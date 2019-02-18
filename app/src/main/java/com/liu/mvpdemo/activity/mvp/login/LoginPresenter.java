package com.liu.mvpdemo.activity.mvp.login;

/**
 * @author liuxuhui
 * @date 2019/2/11
 */
public class LoginPresenter implements LoginContract.Presenter {

    private final static String DEFAULT_NAME = "liuxuhui";
    private final static String DEFAULT_PASSWORD = "123456";

    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void confirmUser(String name, String password) {
        if(DEFAULT_NAME.equals(name) && DEFAULT_PASSWORD.equals(password)) {
            mView.loginSuccess();
        }else {
            mView.loginFailed();
        }
    }
}
