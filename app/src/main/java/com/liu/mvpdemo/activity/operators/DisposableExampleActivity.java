package com.liu.mvpdemo.activity.operators;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Disposable类：
 *   dispose():主动解除订阅
 *   isDisposed():是否解除了订阅
 * CompositeDisposable的使用：
 * 快速接触所有添加的Disposable类，每当得到一个Disposable时就调用；
 * CompositeDisposable.add()：将Disposable添加到容器中
 * CompositeDisposable.clear():退出页面时调用该方法，可快速解除所有订阅
 *
 * defer：直到有观察者订阅时才创建Observable,并且为每个观察者创建一个新的Observable
 * compose 组合操作符，方便调用一组相同的变换（例如一个通用的调用器）
 * @author liuxuhui 
 * @date 2018/12/26
 */
public class DisposableExampleActivity extends AppCompatActivity {
    private static final String TAG = "DisposableActivity";

    @BindView(R.id.disposable_content_tv)
    TextView textView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposable_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.disposable_text)
    public void work(){
        doSomeWork();
    }

    /**
     * defer：直到有观察者订阅时才创建Observable,并且为每个观察者创建一个新的Observable
     */
    private void doSomeWork() {
        compositeDisposable.add(Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                //做一些长时间的操作
                SystemClock.sleep(2000);
                return Observable.just("one", "two", "three", "four");
            }
        }).compose(RxUtil.applyObservableThread())
        .subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                textView.append(s);
                textView.append("\n");
                Log.d(TAG, s +" ");
            }

            @Override
            public void onError(Throwable e) {
                textView.append(e.getMessage());
                Log.d(TAG, e.toString());
            }

            @Override
            public void onComplete() {
                textView.append("onComplete");
                Log.d(TAG, "onComplete");
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
