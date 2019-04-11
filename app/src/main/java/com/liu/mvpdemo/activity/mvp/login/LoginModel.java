package com.liu.mvpdemo.activity.mvp.login;

import com.liu.mvpdemo.activity.util.RxUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

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
    public Observable<String> uploadUserInfo(String name, String password) {
       return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Thread.sleep(2000);
                if("liuxuhui".equals(name) && "123456".equals(password)) {
                    emitter.onNext("成功");
                } else {
                    emitter.onError(new Throwable("失败"));
                }
                emitter.onComplete();
            }
        })
                .compose(RxUtil.applyObservableThread());
    }

    @Override
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
