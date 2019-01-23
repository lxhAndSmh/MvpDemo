package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.bean.Car;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * 创建操作符
 *
 * Defer的使用：
 * 直到有观察者订阅的时候才创建Observable（通过使用Observable工厂方法生成一个新的Observable），并且为每个观察者创建一个新的Observable。
 * 它对每个观察者都这样做，因此尽管每个订阅者都以为自己订阅的是同一个Observable，事实上每个订阅者获取的是它们自己单独的数据序列。
 * 在某些情况下，等待直到订阅发生时才生成Observable，可以确保Observable包含最新的数据。
 *
 * @author liuxuhui
 * @date 2019/1/21
 */
public class CreationExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defer_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                defer();
                break;
            case R.id.text1:
                textView.setText("");
                interval();
                break;
            case R.id.text2:
                textView.setText("");
                range();
                break;
            case R.id.text3:
                textView.setText("");
                repeat();
                break;
            case R.id.text4:
                textView.setText("");
                repeatWhen();
                break;
            case R.id.text5:
                textView.setText("");
                timer();
                break;
            case R.id.text6:
                disposables.clear();
                break;
            default:
                break;
        }

    }

    /**
     * Defer的使用：
     * 直到有观察者订阅的时候才创建Observable（通过使用Observable工厂方法生成一个新的Observable），并且为每个观察者创建一个新的Observable。
     * 它对每个观察者都这样做，因此尽管每个订阅者都以为自己订阅的是同一个Observable，事实上每个订阅者获取的是它们自己单独的数据序列。
     * 在某些情况下，等待直到订阅发生时才生成Observable，可以确保Observable包含最新的数据。
     */
    private void defer() {
        Car car = new Car();
        Observable<String> brandDeferObservable = car.brandDeferObservable();
        car.setBrand("BMW");
        brandDeferObservable.compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(ConstantValues.TAG, "onNext:" + s);
                        textView.append(s + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                        textView.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                });
    }

    /**
     * Interval操作符返回一个Observable，它按固定的时间间隔发射一个无限递增的整数序列。
     * interval(long, TimeUntil): 接受一个表示时间间隔的参数和一个表示时间单位的参数
     * interval(long, long, TimeUntil): 它在指定延迟之后发射零值，然后按照指定的时间间隔发射递增的数字。
     */
    private void interval() {
        disposables.add(Observable.interval(2, 2, TimeUnit.SECONDS)
        .compose(RxUtil.applyObservableThread())
        .subscribeWith(new DisposableObserver<Long>() {

            @Override
            public void onNext(Long aLong) {
                Log.d(ConstantValues.TAG, "onNext:" + aLong);
                textView.append(aLong + " ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                textView.append(e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(ConstantValues.TAG, "onComplete:");
            }
        }));
    }

    /**
     * 返回一个发射特定整数序列的Observable。
     * rang操作符发射一个范围内的有序整数序列，可以指定范围的起始值和长度。
     * rang(int, int):两个参数，一个是范围的起始值，一个是范围的数据的数目。如果第二个参数设为0，将导致Observable不发射任何数据（如果设置为负数，将抛异常）。
     */
    private void range() {
        Observable.range(10, 5)
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(ConstantValues.TAG, "onNext:" + integer);
                        textView.append(integer + " ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                        textView.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                });
    }

    /**
     * repeat():重复地发射数据，它不是创建一个Observable，而是重复发射原始Observable的数据序列，这个序列是无限的。
     * repeat(n):指定重复的次数
     */
    private void repeat() {
        disposables.add(Observable.just(1, 2, 3)
                .repeat(3)
                .compose(RxUtil.applyObservableThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(ConstantValues.TAG, "onNext:" + integer);
                        textView.append(integer + " ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                        textView.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                }));
    }

    /**
     * repeatWhen:它不是缓存和重放原始Observable的数据序列，而是有条件的重新订阅和发射原来的Observable。
     */
    private void repeatWhen() {
        disposables.add(Observable.just(1, 2, 3, 4, 5)
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        //每两秒重复发射一次数据
                        return objectObservable.delay(2, TimeUnit.SECONDS);
                    }
                })
                .compose(RxUtil.applyObservableThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(ConstantValues.TAG, "onNext:" + integer);
                        textView.append(integer + " ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                        textView.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                }));
    }

    /**
     * Timer:创建一个Observable，它在一个给定的延迟后发射一个简单的数字0
     */
    private void timer() {
        Observable.timer(5, TimeUnit.SECONDS)
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(ConstantValues.TAG, "onNext:" + aLong);
                        textView.append(aLong + " ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                        textView.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                });
    }
}
