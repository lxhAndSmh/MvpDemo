package com.liu.mvpdemo;

import android.app.Application;

import com.liu.mvpdemo.activity.rxbus.RxBus;

/**
 * @author liuxuhui
 * @date 2019/1/25
 */
public class MvpApplication extends Application {

    private RxBus rxBus;

    @Override
    public void onCreate() {
        super.onCreate();
        rxBus = new RxBus();
    }

    public RxBus bus() {
        return rxBus;
    }
}
