package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;
/**
 * Replay的使用：
 *
 * 保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅。
 *
 * 可连接的Observable（ConnectableObservable）与普通的Observable差不多，只不过它并不会在被订阅时开始发射数据，
 * 而是直接使用了Connect操作符时，才开始发射数据。
 *
 * 变体replay(int)和replay(long， TimeUnit):指定replay的最大缓存量。
 *
 * @author liuxuhui
 * @date 2019/1/21
 */
public class ReplayExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.text)
    public void doSomeWork() {
        PublishSubject subject = PublishSubject.create();
        ConnectableObservable connectableObservable = subject.replay();
        connectableObservable.connect();

        connectableObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(ConstantValues.TAG, "first Observer onSubscribe:" + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(ConstantValues.TAG, "first Observer onNext:" + integer);
                textView.append(integer + " ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(ConstantValues.TAG, "first Observer onError:" + e.getMessage());
                textView.append("first Observer onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(ConstantValues.TAG, "first Observer onComplete:");
                textView.append(" first Observer");
            }
        });

        subject.onNext(1);
        subject.onNext(2);
        subject.onNext(3);
        subject.onNext(4);
        subject.onComplete();

        /*
         * 如果使用的replay（3）的变体，it will emit 2, 3, 4 as (count = 3), retains the 3 values for replay
         */
        connectableObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(ConstantValues.TAG, "second Observer onSubscribe:" + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(ConstantValues.TAG, "second Observer onNext:" + integer);
                textView.append(integer + " ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(ConstantValues.TAG, "second Observer onError:" + e.getMessage());
                textView.append("second Observer onError:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(ConstantValues.TAG, "second Observer onComplete:");
                textView.append(" second Observer");
            }
        });

    }
}
