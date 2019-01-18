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
/**
 * skip的使用：
 *
 * skip操作符，你可以忽略Observable发射的前N项数据，只保留之后的数据。
 * skip（long, TimeUnit): skip的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable开始的那段
 * 时间发射的数据，时长和时间单位通过参数指定
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

    @OnClick({R.id.text, R.id.text1})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                sikp();
                break;
            case R.id.text1:
                textView.setText("");
                sikpByTime();
                break;
            default:
                break;
        }
    }

    /**
     * skip操作符，你可以忽略Observable发射的前N项数据，只保留之后的数据。
     */
    private void sikp() {
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
    private void sikpByTime() {
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
}
