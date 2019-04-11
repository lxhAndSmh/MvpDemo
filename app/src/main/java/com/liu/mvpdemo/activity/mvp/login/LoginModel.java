package com.liu.mvpdemo.activity.mvp.login;

import android.util.Log;

import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author liuxuhui
 * @date 2019/4/10
 */
public class LoginModel implements LoginContract.Model {

    /**
     * 开启一个线程，模拟网络请求
     * @param name
     * @param password
     */
    @Override
    public void uploadUserInfo(String name, String password, NetworCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if("liuxuhui".equals(name) && "123456".equals(password)) {
                    emitter.onNext("成功");
                    Log.d(ConstantValues.TAG, "----1-----");
                } else {
                    emitter.onError(new Throwable("失败"));
                    Log.d(ConstantValues.TAG, "----2-----");
                }
                emitter.onComplete();
            }
        })
                .delay(1, TimeUnit.SECONDS)
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(String s) {
                        callBack.onSuccess("成功");
                        Log.d(ConstantValues.TAG, "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFail(500, "失败");
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        callBack.onFail(500, "失败");
                        Log.d(ConstantValues.TAG, "onComplete");
                    }
                });
    }

    public void uploadUserInfoByThread(String name, String password, NetworCallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    callBack.onFail(500, "失败");
                }
                if("liuxuhui".equals(name) && "123456".equals(password)) {
                    callBack.onSuccess("成功");
                }else {
                    callBack.onFail(500, "失败");
                }
            }
        }).start();
    }
}
