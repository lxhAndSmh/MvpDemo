package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * skip的使用：
 *
 * skip操作符，你可以忽略Observable发射的前N项数据，只保留之后的数据。
 * skip（long, TimeUnit): skip的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable开始的那段
 * 时间发射的数据，时长和时间单位通过参数指定
 *
 * skipLast操作符，你可以忽略Observable发射的后N项数据，只保留之前的数据。
 * skipLast（long, TimeUnit): skipLast的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable结束的那段
 * 时间发射的数据，时长和时间单位通过参数指定
 *
 * SkipUntil操作符，丢弃原始Observable发射的数据，直到第二个Observable发射了一项数据
 * SkipUntil订阅原始的Observable，但是忽略它的发射物，直到第二个Observable发射了一项数据那一刻，它开始发射原始Observable。
 *
 * SkipWhile操作符，丢弃Observable发射的数据，直到一个指定的条件不成立。
 * SkipWhile订阅原始的Observable，但是忽略它的发射物，直到你指定的某个条件变为false的那一刻，它开始发射原始Observable
 *
 * @author liuxuhui
 * @date 2019/1/18
 */
public class SkipExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                skip();
                break;
            case R.id.text1:
                textView.setText("");
                skipByTime();
                break;
            case R.id.text2:
                textView.setText("");
                skipLast();
                break;
            case R.id.text3:
                textView.setText("");
                skipUntil();
                break;
            case R.id.text4:
                textView.setText("");
                skipWhile();
                break;
            default:
                break;
        }
    }

    /**
     * skip操作符，你可以忽略Observable发射的前N项数据，只保留之后的数据。
     */
    private void skip() {
        Observable.just(1, 2, 3, 4, 5)
                .skip(2)
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
     * skip（long, TimeUnit): skip的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable开始的那段
     * 时间发射的数据，时长和时间单位通过参数指定
     */
    private void skipByTime() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emitter.onNext(1);
                    }
                }, 500);

                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emitter.onNext(2);
                    }
                }, 1000);

                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        emitter.onNext(3);
                    }
                }, 1500);
            }
        })
                .skip(1000, TimeUnit.MILLISECONDS)
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
     * skipLast操作符，你可以忽略Observable发射的后N项数据，只保留之前的数据。
     * skipLast（long, TimeUnit): skipLast的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable结束的那段
     * 时间发射的数据，时长和时间单位通过参数指定
     */
    private void skipLast() {
        Observable.just(1, 2, 3, 4, 5)
                .skipLast(2)
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
     * SkipUntil操作符，丢弃原始Observable发射的数据，直到第二个Observable发射了一项数据
     * SkipUntil订阅原始的Observable，但是忽略它的发射物，直到第二个Observable发射了一项数据那一刻，它开始发射原始Observable。
     */
    private void skipUntil() {
        Observable.intervalRange(0, 10, 0, 1, TimeUnit.SECONDS)
                //3秒后发射数据，输出的是3，4，5，6，7，8，9
                .skipUntil(Observable.timer(3, TimeUnit.SECONDS))
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(Long integer) {
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
     * SkipWhile操作符，丢弃Observable发射的数据，直到一个指定的条件不成立。
     * SkipWhile订阅原始的Observable，但是忽略它的发射物，直到你指定的某个条件变为false的那一刻，它开始发射原始Observable
     */
    private void skipWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 3;
                    }
                })
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        //输出值为3，4，5
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
}
