package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;
import com.liu.mvpdemo.activity.util.Utils;
import com.liu.mvpdemo.bean.ApiUser;
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
import io.reactivex.functions.Function;

/**
 * Map操作符的使用：
 * Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable
 * @author liuxuhui
 * @date 2019/1/17
 */
public class MapExampleActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_example);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void doSomeWork(){
        Observable.create(new ObservableOnSubscribe<List<ApiUser>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ApiUser>> emitter) throws Exception {
                emitter.onNext(Utils.getApiUserList());
                emitter.onComplete();
            }
        })
                .compose(RxUtil.applyObservableThread())
                .map(new Function<List<ApiUser>, List<User>>() {
                    @Override
                    public List<User> apply(List<ApiUser> apiUsers) throws Exception {
                        return Utils.convertApiUserListToUserList(apiUsers);
                    }
                })
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<User> users) {
                        textView.append("onNext");
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
}
