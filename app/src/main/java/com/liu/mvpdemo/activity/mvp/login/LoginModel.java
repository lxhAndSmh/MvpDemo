package com.liu.mvpdemo.activity.mvp.login;

import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.concurrent.TimeUnit;

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
    public void uploadUserInfo(String name, String password, NetworCallBack callBack) {
        Observable.create(new ObservableOnSubscribe<NetworCallBack>() {

            @Override
            public void subscribe(ObservableEmitter<NetworCallBack> emitter) throws Exception {
                emitter.onNext(callBack);
                emitter.onComplete();
            }
        })
                .delay(3, TimeUnit.SECONDS)
                .compose(RxUtil.applyObservableThread())
                .subscribe(callBack1 -> {callBack1.onSuccess("成功");}
                ,throwable -> {callBack.onFail(500, "失败");});
    }
}
