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
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * 算术和聚合操作:
 * Reduce/Concat
 *
 * @author liuxuhui
 * @date 2019/1/18
 */
public class ReduceExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduce_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                concat();
                break;
            case R.id.text1:
                reduce();
                break;
            default:
                break;
        }
    }

    /**
     * concat：不交错的发射两个或多个Observable发射的数据
     * Concat操作符链接多个Observable的输出，第一个Observable发射的所有数据在第二个Observable发射的任何数据前面，以此类推。
     * merge操作符和concat差不多，它结合两个或多个Observable的发射物，但是数据可能交错，而Concat不会让多个Observable的发射物交错。
     * 还有一个实例方法叫concatWith，这两者是等价的：Observable.concat(a,b)和a.concatWith(b)。
     */
    private void concat() {
        Observable<Integer> observable = Observable.just(5, 6, 7);
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable.concat(observable, observable1)
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
     * reduce(Func2)：按顺序对Observable发射的每项数据应用一个函数并发射最终的值。
     * 注意：如果原始Observable没有发射任何数据，reduce抛出异常IllegalArgumentException
     * reduce(seed, Func2）：接受一个种子参数
     */
    private void reduce() {
        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .compose(RxUtil.applyMaybeThread())
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d(ConstantValues.TAG, "onSuccess:" + integer);
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
