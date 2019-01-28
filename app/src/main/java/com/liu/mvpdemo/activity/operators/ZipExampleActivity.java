package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.activity.util.Utils;
import com.liu.mvpdemo.bean.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

/**
 * zip的使用:
 * zip操作符返回一个Observable,它使用这个函数按顺序结合两个或多个Observables发射的数据项，然后
 * 它发射这个函数返回的结果；zip的最后一个参数接收每个Observable发射的数据，返回被压缩后的数据（最多可以有九个Observable参数）
 * @author liuxuhui
 * @date 2019/1/17
 */
public class ZipExampleActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void doSomeWork(){
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(), new BiFunction<List<User>, List<User>, List<User>>() {
            @Override
            public List<User> apply(List<User> users, List<User> users2) throws Exception {
                users.addAll(users2);
                return users;
            }
        })
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        textView.append("onNext:");
                        for(User user : users) {
                            textView.append(user.firstname + "\n");
                        }
                        Log.d(ConstantValues.TAG, "onNext:" + users.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError:" + e.getMessage());
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete");
                    }
                });
    }

    private Observable<List<User>> getCricketFansObservable() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
               if(!emitter.isDisposed()) {
                   emitter.onNext(Utils.getUserListWhoLovesCricket());
                   emitter.onComplete();
               }
            }
        });
    }

    private Observable<List<User>> getFootballFansObservable() {
        return Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                if(!emitter.isDisposed()) {
                    emitter.onNext(Utils.getUserListWhoLovesFootball());
                    emitter.onComplete();
                }
            }
        });
    }
}
