package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.bean.Car;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
/**
 * Defer的使用：
 * 直到有观察者订阅的时候才创建Observable（通过使用Observable工厂方法生成一个新的Observable），并且为每个观察者创建一个新的Observable。
 * 它对每个观察者都这样做，因此尽管每个订阅者都以为自己订阅的是同一个Observable，事实上每个订阅者获取的是它们自己单独的数据序列。
 * 在某些情况下，等待直到订阅发生时才生成Observable，可以确保Observable包含最新的数据。
 *
 * @author liuxuhui
 * @date 2019/1/21
 */
public class DeferExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defer_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.text)
    public void doSomeWork() {
        Car car = new Car();
        Observable<String> brandDeferObservable = car.brandDeferObservable();
        car.setBrand("BMW");
        brandDeferObservable.compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(ConstantValues.TAG, "onNext:" + s);
                        textView.append(s + "\n");
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
