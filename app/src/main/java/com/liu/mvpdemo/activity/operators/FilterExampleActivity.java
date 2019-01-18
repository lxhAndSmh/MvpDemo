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
 * Filter操作符的使用：
 *
 * Filter操作符，使用你指定的一个函数测试数据项，只有通过测试的数据才会发射。
 * ofType是filter操作符的一个特殊形式，它过滤一个Observable只返回指定类型的数据。
 *
 * @author liuxuhui
 * @date 2019/1/18
 */
public class FilterExampleActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_example);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text, R.id.text1})
    public void doSomeWork(View view) {
        switch (view.getId()) {
            case R.id.text:
                textView.setText("");
                filter();
                break;
            case R.id.text1:
                textView.setText("");
                ofType();
                break;
            default:
                break;
        }
    }

    /**
     * Filter操作符，使用你指定的一个函数测试数据项，只有通过测试的数据才会发射。
     */
    private void filter() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        //过滤出偶数
                        return integer % 2 == 0;
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
     * ofType是filter操作符的一个特殊形式，它过滤一个Observable只返回指定类型的数据。
     */
    private void ofType() {
        Observable.just(1, "hi", 3, "tom")
                .ofType(Integer.class)
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
