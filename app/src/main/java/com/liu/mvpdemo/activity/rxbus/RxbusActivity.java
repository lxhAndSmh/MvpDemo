package com.liu.mvpdemo.activity.rxbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.liu.mvpdemo.MvpApplication;
import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.bean.event.MessageEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * RxBus的使用
 * @author liuxuhui
 * @date 2019/1/25
 */
public class RxbusActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);
        ButterKnife.bind(this);
        disposables.add(((MvpApplication)getApplication())
                .bus()
                .toObservable()
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if(o instanceof MessageEvent) {
                            textView.setText((((MessageEvent) o).msg));
                        }
                    }
                }));
    }

    @OnClick(R.id.textView)
    public void doSomeWork() {
        ((MvpApplication)getApplication()).bus().send(new MessageEvent("发送Event"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
