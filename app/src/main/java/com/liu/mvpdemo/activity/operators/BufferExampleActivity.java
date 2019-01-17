package com.liu.mvpdemo.activity.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.util.ConstantValues;
import com.liu.mvpdemo.activity.util.RxUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Buffer的使用:
 * 定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。
 * Buffer操作符将一个Observable变换成另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合；
 * 每一个缓存至多包含来自原始Observable的count项数据（最后发射的数据可能少于count项）
 *
 * 注意：如果原来的Observable发射一个onError通知，Buffer会立即传递这个通知，而不是首先发射缓存数据，即使在之前的缓存中
 * 包含了原始Observable发射的数据。
 *
 * buffer(count, skip)从原始Observable的第一项数据开始创建新的缓存，用count项数据填充缓存（开头的一项和count-1项中间的数据，
 * 然后以列表List的形式发射缓存，这些缓存可能有重叠部分（比如skip < count时），也可能有间隙（如：skip > count时)
 *
 * buffer(count) 不传skip时，缓存的数据不会有重叠,等效于传递一个count相同值的skip
 *
 * @author liuxuhui
 * @date 2019/1/17
 */
public class BufferExampleActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer_example);
        ButterKnife.bind(this);
    }

    /**
     * count:表示会取最多取count个数据，放到一个List集合中
     * skip:表示每次跳过skip个数据
     * 所以下面例子的集合是：
     * 1 - one, two , three
     * 2 - two, three, four
     * 3 - three, four, five
     * 4 - four, five
     * 5 - five
     */
    @OnClick(R.id.button)
    public void doSomeWork() {
        Observable.just("one", "two", "three", "four", "five")
                .buffer(3, 1)
                .compose(RxUtil.applyObservableThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(ConstantValues.TAG, "onSubscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        textView.append("\nonNext size: " + strings.size() + "\n");
                        Log.d(ConstantValues.TAG, "onNext size: :" + strings.size());
                        for(String str : strings) {
                            textView.append(" " + str);
                            Log.d(ConstantValues.TAG, " " + str);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append("onError:" + e.getMessage());
                        Log.d(ConstantValues.TAG, "onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(ConstantValues.TAG, "onComplete:");
                    }
                });
    }
}
