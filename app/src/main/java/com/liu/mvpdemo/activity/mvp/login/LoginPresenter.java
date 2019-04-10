package com.liu.mvpdemo.activity.mvp.login;

import android.util.Log;

import com.liu.mvpdemo.activity.util.ConstantValues;

/**
 * @author liuxuhui
 * @date 2019/2/11
 */
public class LoginPresenter implements LoginContract.Presenter {

    private final static String DEFAULT_NAME = "liuxuhui";
    private final static String DEFAULT_PASSWORD = "123456";

    private LoginContract.View mView;
    private LoginContract.Model mModel;

    public LoginPresenter() {
    }

    public LoginPresenter(LoginContract.View mView, LoginContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;
    }

    @Override
    public void confirmUser(String name, String password) {
        if(DEFAULT_NAME.equals(name) && DEFAULT_PASSWORD.equals(password)) {
            mView.loginSuccess();
        }else {
            mView.loginFailed();
        }
    }

    @Override
    public void loginByNetwork(String name, String password) {
        mModel.uploadUserInfo(name, password, new NetworCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.d(ConstantValues.TAG, "onSuccess:" + data.toString());
            }

            @Override
            public void onFail(int code, String msg) {
                Log.d(ConstantValues.TAG, msg + code);
            }
        });
    }

    public boolean checkoutInfo(String name, String password) {
        if(DEFAULT_NAME.equals(name) && DEFAULT_PASSWORD.equals(password)) {
            return true;
        }else {
            return false;
        }
    }
}
