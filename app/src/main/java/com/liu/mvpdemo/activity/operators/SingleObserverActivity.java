package com.liu.mvpdemo.activity.operators;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import org.reactivestreams.Subscription;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Single的使用:
 * Single是Obserable的变种，它总是只发射一个值，或者一个错误的通知，而不是发射一系列的值。
 * 因此，订阅Single只需要两个方法：
 * onSuccess Single发射单个值到这个方法
 * onError 如果无法发射需要的值，Single发射一个Throwable对象到这个方法
 * Single只会调用这两个方法中的一个，而且只会调用一次，调用了任何一个方法之后，订阅关系终止
 * @author liuxuhui
 * @date 2019/1/11
 */
public class SingleObserverActivity extends AppCompatActivity {
    @BindView(R.id.textView2)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_observer);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.button2:
                singleJust();
                break;
            case R.id.button3:
                singleConcat();
                break;
            case R.id.button4:
                singleFlatMap();
                break;
            case R.id.button5:
                singleFlatObservable();
                break;
            case R.id.button6:
                singleMerge();
                break;
            default:
                break;
        }
    }

    /**
     * just：返回一个发射指定值的Single
     */
    private void singleJust() {
        Single.just("hello")
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(String value) {
                        textView.append(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        textView.append(error.getMessage());
                    }
                });
    }

    /**
     * crate：调用观察者的方法创建一个Single
     * concat: 返回FlowableSubscriber或者Subscriber，用于连接多个Single或者Observable发射的数据
     * compose: 创建一个自定义的操作符（例:返回一个通用的线程调度器）
     * error: 返回一个立即给订阅者发送错误通知的Single
     */
    private void singleConcat() {
        Single<String> single = Single.create(f -> f.onSuccess("hello"));
        Single<String> single1 = Single.create(f -> f.onSuccess("world"));
        Single<Throwable> single2 = Single.error(new Throwable("错误通知"));
        Single.concat(single,  single1, single2)
                .compose(RxUtil.applyFlowableThread())
                .subscribe(new FlowableSubscriber<Object>() {
                    Subscription sub;
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub = s;
                        sub.request(4);
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(ConstantValues.TAG, "\nonNext:" + o.toString());
                        textView.append(o.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(ConstantValues.TAG, "\nonError:" + t.getMessage());
                        textView.append(t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete");
                    }
                });
    }

    /**
     * map:返回一个Single,它发射一个指定值的Single
     * flatMap:返回一个Single，它发射对原Single的数据执行flatMap操作后的结果
     */
    private void singleFlatMap() {
        Single.just("520")
                .map(mapper -> "1314")
                .flatMap(new Function<String, SingleSource<Integer>>() {
                    @Override
                    public SingleSource<Integer> apply(String s) throws Exception {
                        return Single.just(Integer.parseInt(s));
                    }
                })
                .compose(RxUtil.applySingleThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        textView.append(" success:" + integer);
                        Log.d(ConstantValues.TAG, "onSuccess:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append(" error:" + e.getMessage());
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                    }
                });
    }

    /**
     * fromFuture: 将Future转换成Single；fromFuture有一个可接受两个可选参数的方法，分别指定超时时长和时间单位，
     *            如果过了指定的时长，Future还没返回一个值，这个Observable会发射错误通知并终止
     * Future:可以看成总是只发射单个数据的Observable；对于Future，它会发射Future.get()方法返回的单个数据
     * flapMapObservable:返回一个DisposableObserver,它发射对原Single的数据执行FlatMap操作后的结果
     */
    private void singleFlatObservable() {
        Single.fromFuture(new Future<Integer>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Integer get() throws InterruptedException, ExecutionException {
                return 1314;
            }

            @Override
            public Integer get(long timeout, @NonNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return 1315;
            }
        }, 3, TimeUnit.SECONDS)
                .flatMapObservable(mapper -> Observable.just(mapper + 1))
                .compose(RxUtil.applyObservableThread())
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        textView.append(" success:" + integer);
                        Log.d(ConstantValues.TAG, "onSuccess:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append(" error:" + e.getMessage());
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete");
                    }
                });
    }

    /**
     * merge and mergeWith:返回FlowableSubscriber，合并发射来自多个Single的数据
     */
    private void singleMerge() {

        Single<String> single = Single.create(f -> f.onSuccess("Hi"));
        Single<String> single1 = Single.create(f -> f.onSuccess(" girl!"));
        Single<String> single2 = Single.create(f -> f.onSuccess(" you"));
        Single<String> single3 = Single.create(f -> f.onSuccess(" are"));
        Single<String> single4 = Single.create(f -> f.onSuccess(" so"));
        Single<String> single5 = Single.create(f -> f.onSuccess(" beautiful!"));
        Single.merge(single, single1, single2, single3)
                .compose(RxUtil.applyFlowableThread())
                .subscribe(new FlowableSubscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(4);
                    }

                    @Override
                    public void onNext(String s) {
                        textView.append(" " + s);
                        Log.d(ConstantValues.TAG, "onSuccess:" + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        textView.append(" error:" + t.getMessage());
                        Log.d(ConstantValues.TAG, "onError:" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete");
                    }
                });
    }
}
