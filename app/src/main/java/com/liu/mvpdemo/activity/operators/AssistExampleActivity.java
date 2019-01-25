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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 辅助操作
 *
 *
 * Delay 延迟一段指定的时间再发射来自Observable的发射物。
 * delay(long, TimeUnit):Delay操作符让原始Observable在发射每项数据之前都暂停一段指定的时间段。效果是Observable发射的数据项
 * 在时间上向前整体平移了一个增量。
 * （注意：delay不会平移onError通知，它会立即将这个通知传递给订阅者，同事丢弃任何发射onNext的通知。然而它会平移一个onCompleted通知）
 *
 * TimeOut：对原始Observable的一个镜像，如果过了一个指定的时长仍没有发射数据，它会发射一个错误通知。
 *
 * @author liuxuhui
 * @date 2019/1/24
 */
public class AssistExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assist_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                delay();
                break;
            case R.id.text1:
                textView.setText("");
                timeOut();
                break;
            default:
                break;
        }

    }

    /**
     * 延迟一段指定的时间再发射来自Observable的发射物。
     * delay(long, TimeUnit):Delay操作符让原始Observable在发射每项数据之前都暂停一段指定的时间段。效果是Observable发射的数据项
     * 在时间上向前整体平移了一个增量。
     * （注意：delay不会平移onError通知，它会立即将这个通知传递给订阅者，同事丢弃任何发射onNext的通知。然而它会平移一个onCompleted通知）
     */
    private void delay() {
        Observable.just(1, 2, 3, 4, 5)
                .delay(5, TimeUnit.SECONDS)
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
     * 对原始Observable的一个镜像，如果过了一个指定的时长仍没有发射数据，它会发射一个错误通知。
     */
    private void timeOut() {
        Observable.just(1, 2, 3, 4, 5)
                //延时6秒发射数据
                .delay(6, TimeUnit.SECONDS)
                //监控如果超过5秒没有发射数据，将发射错误通知
                .timeout(5, TimeUnit.SECONDS)
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
