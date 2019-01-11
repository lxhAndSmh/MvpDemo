package com.liu.mvpdemo.activity.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.liu.mvpdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
/**
 * Single的使用:
 * Single是Obserable的变种，它总是只发射一个值，或者一个错误的通知，而不是发射一系列的值。
 * 因此，订阅Single只需要两个方法：
 * onSuccess Single发射单个值到这个方法
 * onError 如果无法发射需要的值，Single发射一个Throwable对象到这个方法
 * Single只会调用这两个方法中的一个，而且只会调用一次，调用了任何一个方法之后，订阅关系终止
 * @author liuxuhui
 * @date 2019/1/11
 */
public class SingleObserverActivity extends AppCompatActivity {

    @BindView(R.id.textView2)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_observer);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button2)
    public void doSomeWork() {
        Single.just("hello")
                .subscribe(new SingleSubscriber<String>() {

                    @Override
                    public void onSuccess(String value) {
                        textView.append(value);
                    }

                    @Override
                    public void onError(Throwable error) {
                        textView.append(error.getMessage());
                    }
                });
    }
}
