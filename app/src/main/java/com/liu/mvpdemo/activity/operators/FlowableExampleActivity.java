package com.liu.mvpdemo.activity.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.RxUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
/**
 * 使用Flowable和reduce操作
 * reduce:按顺序对Observable发射的每项数据应用一个函数并发射最终的值。
 * （操作符对Observable发射数据的第一项应用到一个函数，然后再将返回的值与第二项数据一起传递给函数，
 *  以此类推，持续到最后一项数据并停止。并返回这个函数的最终值）
 * @author liuxuhui
 * @date 2019/1/8
 */
public class FlowableExampleActivity extends AppCompatActivity {
    public static String TAG = "FlowableExampleActivity";

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void doSomeWork() {
        Flowable.just(1, 2, 3, 4)
                //第一个参数是初始值
                .reduce(50, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .compose(RxUtil.applySingleThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        textView.append("onSuccess : value: " + integer + "\n");
                        Log.d(TAG,"onSuccess : value: " + integer);
                        //输出的值是60
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError: " + e.getMessage());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
        Flowable.just(2, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        textView.append("onSuccess : value: " + integer + "\n");
                        Log.d(TAG,"onSuccess : value: " + integer);
                        //输出的值是11
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError: " + e.getMessage());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete:");
                    }
                });
    }
}
