package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * Completable: 用于不需要知道任何返回值，只需要知道是否结束或错误
 * 使用场景:例如请求接口更新服务端数据，我们只需要知道是否更新成功，不需要知道更新后返回的数据。
 * @author liuxuhui
 * @date 2019/1/16
 */
public class CompletableObserverExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView3)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completable_obser_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button8)
    public void dosomeWork() {
        Completable.timer(1, TimeUnit.SECONDS)
                .compose(RxUtil.applyCompletableThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, " onSubscribe : " + d.isDisposed());
                    }

                    @Override
                    public void onComplete() {
                        textView.append(" onComplete ");
                        Log.d(ConstantValues.TAG, " onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append(" onError " + e.getMessage());
                        Log.d(ConstantValues.TAG, " onError");
                    }
                });
    }
}
