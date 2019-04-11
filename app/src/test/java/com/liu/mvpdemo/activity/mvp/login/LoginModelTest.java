package com.liu.mvpdemo.activity.mvp.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author liuxuhui
 * @date 2019/4/11
 */
@RunWith(RobolectricTestRunner.class)
public class LoginModelTest {

    @Test
    public void uploadUserInfo() {

    }

    /**
     * 测试异步方法:成功
     */
    @Test
    public void uploadUserInfoByThreadSuccess() throws InterruptedException {
        LoginModel model = new LoginModel();
        final String[] result = {""};
        CountDownLatch latch = new CountDownLatch(1);
        model.uploadUserInfoByThread("liuxuhui", "123456", new NetworCallBack() {
            @Override
            public void onSuccess(Object data) {
                result[0] = data.toString();
                latch.countDown();
            }

            @Override
            public void onFail(int code, String msg) {
                result[0] = msg;
                latch.countDown();
            }
        });
        latch.await(4, TimeUnit.SECONDS);
        assertEquals("成功", result[0]);
    }


    /**
     * 测试异步方法:失败
     */
    @Test
    public void uploadUserInfoByThreadFail() throws InterruptedException {
        LoginModel model = new LoginModel();
        final String[] result = {""};
        CountDownLatch latch = new CountDownLatch(1);
        model.uploadUserInfoByThread("", "123456", new NetworCallBack() {
            @Override
            public void onSuccess(Object data) {
                result[0] = data.toString();
                latch.countDown();
            }

            @Override
            public void onFail(int code, String msg) {
                result[0] = msg;
                latch.countDown();
            }
        });
        latch.await(4, TimeUnit.SECONDS);
        assertEquals("失败", result[0]);
    }
}