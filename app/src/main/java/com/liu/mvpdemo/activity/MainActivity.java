package com.liu.mvpdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.operators.DisposableExampleActivity;
import com.liu.mvpdemo.activity.operators.FlowableExampleActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.text_content)
    TextView textContext;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mStringBuilder = new StringBuilder();
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.text:
                initFlowable();
                break;
            case R.id.text1:
                initFlowableCreate();
                break;
            case R.id.text2:
                initMaybe();
                break;
            case R.id.text3:
                intent = new Intent(this, DisposableExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.text4:
                intent = new Intent(this, FlowableExampleActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Flowable/Subscriber
     */
    private void initFlowable() {
        Flowable.range(1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    Subscription subscription;
                    @Override
                    public void onSubscribe(Subscription s) {
                        //Subscription参数可以用去请求数据或者取消订阅
                        Log.d(TAG, "onSubscribe start");
                        mStringBuilder = new StringBuilder();
                        subscription = s;
                        s.request(1);
                        Log.d(TAG, "onSubscribe end");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext:" + integer);
                        mStringBuilder.append(integer);
                        subscription.request(1);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        textContext.setText(mStringBuilder.toString());
                        //取消订阅
                        subscription.cancel();
                    }
                });
    }

    /**
     * Flowable 通过create创建时要指定背压策略
     */
    private void initFlowableCreate() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscription -> {progressBar.setVisibility(View.VISIBLE);})
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //确保再request（）之前已经完成所有的初始化工作，避免空指针问题
                        mStringBuilder = new StringBuilder();
                        s.request(4);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mStringBuilder.append(integer);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        textContext.setText(mStringBuilder.toString());
//                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    /**
     * Maybe/MaybeObaserver 不能用于发送大量数据，只用于发送单条数据（只想要某个事件的结果时，可以使用这个观察者模式）
     */
    private void initMaybe() {
        Maybe.create(new MaybeOnSubscribe<Integer>() {
            @Override
            public void subscribe(MaybeEmitter<Integer> emitter) throws Exception {
                // emitter.onSuccess(1) 和 emitter.onComplete()只能执行其中一个
                emitter.onSuccess(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        if(integer == 1) {
                            Log.d(TAG, "成功");
                        }else {
                            Log.d(TAG, "失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "结束");
                    }
                });
    }
}
