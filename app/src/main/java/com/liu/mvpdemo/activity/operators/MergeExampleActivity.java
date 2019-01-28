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
/**
 *
 * Merge的使用：
 * 合并多个Observable的发射物。任何一个原始的Observable的onError通知会被立即传递给观察者，而且会终止合并后的Observable
 * merge可能让合并的Observables发射的数据交错（类似的操作符Concat不会让数据交错，它会按顺序一个接一个发射多个Observables的发射物）
 *
 * merge是静态方法，mergeWith是对象方法：Observable.merge(obser1, obser2)等价于Obser1.mergeWith(obser2)
 *
 * @author liuxuhui
 * @date 2019/1/21
 */
public class MergeExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1})
    public void doSomeWork(View view) {
        String[] aStrings = {"A1", "A2", "A3", "A4"};
        String[] bStrings = {"B1", "B2", "B3"};

        Observable<String> aObservable = Observable.fromArray(aStrings);
        Observable<String> bObservable = Observable.fromArray(bStrings);
        switch (view.getId()) {
            case R.id.text:

                Observable.merge(aObservable, bObservable)
                        .compose(RxUtil.applyObservableThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                                textView.append("\n merge: ");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d(ConstantValues.TAG, "onNext:" + s);
                                textView.append(s + " ");
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
                break;
            case R.id.text1:

                aObservable.mergeWith(bObservable)
                        .compose(RxUtil.applyObservableThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(ConstantValues.TAG, "mergeWith onSubscribe:" + d.isDisposed());
                                textView.append("\n mergeWith: ");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d(ConstantValues.TAG, "mergeWith onNext:" + s);
                                textView.append(s + " ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(ConstantValues.TAG, "mergeWith onError:" + e.getMessage());
                                textView.append(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(ConstantValues.TAG, "mergeWith onComplete:");
                            }
                        });
                break;
            default:
                break;
        }
    }
}
