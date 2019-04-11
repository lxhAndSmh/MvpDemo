package com.liu.mvpdemo.activity.mvp.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 *
 * 异步的单元测试有两种方法：
 * 1.等待异步代码执行完了，再执行assert操作。
 * 2.将异步变成同步
 * @author liuxuhui
 * @date 2019/4/11
 */
@RunWith(RobolectricTestRunner.class)
public class LoginModelTest {

    /**
     * 将异步变成同步
     */
    @Test
    public void uploadUserInfo() {
        RxJavaPlugins.reset();
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });
        LoginModel model = new LoginModel();
        final String[] result = new String[1];
        model.uploadUserInfo("liuxuhui", "123456")
                .subscribe(str -> result[0] = str);
        assertEquals("成功", result[0]);
    }

    /**
     * 等待异步代码执行完了，再执行assert操作。
     * CountDownLatch类中的await()方法会阻塞当前线程，直到countDown()被调用了一定的次数，这个次数就是在创建这个CountDownLatch对象时，传入的构造参数；
     * CountDownLatch的工作原理类似于倒序计数，刚开始设定了一个数字，每次countDown()这个数字减1，await()方法会一直等待，直到这个数字为0；
     * await()还有一个重载方法，可以用来指定你要等待多久，因为很多时候你不想一直等下去。
     *
     * 缺点：
     * 1.countDown()必须可以在测试代码里面写（即必需有Callback）
     * 2.写起来有点罗嗦，创建对象、调用countDown()、调用await()都必须手动写，而且还没有通用性，你没有办法抽出一个类或方法来简化代码。
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
        //等待2秒钟，如果4秒以后，计数是0了，则返回True，否则返回False。
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