package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Take和TakeLast的使用：
 *
 * take(n)使用Take操作符可以修改Observable的行为，只发射前面的N项数据,然后发射完成通知，忽略剩余的数据。
 * 如果Observable发射的数据少于N项，take操作生成的Observable不会抛异常或发射onError通知，它会发射相同的少量数据
 *
 * takeLast（int）操作符：发射Observable的最后N项数据
 *
 * @author liuxuhui
 * @date 2019/1/18
 */
public class TakeExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                take();
                break;
            case R.id.text1:
                textView.setText("");
                takeLast();
                break;
            case R.id.text2:
                textView.setText("");
                takeUntil();
                break;
            case R.id.text3:
                textView.setText("");
                takeWhile();
                break;
            default:
                break;
        }
    }

    /**
     * take操作符：发射Observable的前N项数据
     * 变体take(long, TimeUnit): 这个变体接收的是一个时长而不是数量参数。它会发射Observable开始的那段时间发射的数据，
     * 时长和时间单位通过参数指定。
     */
    private void take() {
        Observable.just(1, 2, 3, 4, 5)
                .take(3)
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
     * takeLast（int）操作符：发射Observable的最后N项数据
     * 变体takeLast(long, TimeUnit): 这个变体接收的是一个时长而不是数量参数。它会发射Observable结束的那段时间发射的数据，时长和时间单位通过参数指定。
     */
    private void takeLast() {
        Observable.just(1, 2, 3, 4, 5)
                .takeLast(2)
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
     * takeUntil：发送complete的结束条件，发送结束之前也会包括这个值
     */
    private void takeUntil() {
        Observable.just(1, 2, 3, 4, 5)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        //会发射4
                        return integer > 3;
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
     * takeWhile：当不满足这个条件时，会发送结束，切不包括这个值
     */
    private void takeWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        //会发射4
                        return integer <= 4;
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
