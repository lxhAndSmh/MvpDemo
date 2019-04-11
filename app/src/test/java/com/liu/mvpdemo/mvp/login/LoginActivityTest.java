package com.liu.mvpdemo.mvp.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.mvp.home.MainActivity;
import com.liu.mvpdemo.activity.mvp.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author liuxuhui
 * @date 2019/2/11
 */

@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private EditText nameEt;
    private EditText passwordEt;
    private Button button;
    private Activity activity;

    /**
     * 创建activity的实例
     * setupActivity is Deprecated
     * 替代方法 Robolectric.buildActivity(LoginActivity.class).create().get();
     */
    @Before
    public void setUp() {
//        activity = Robolectric.setupActivity(LoginActivity.class);
        activity = Robolectric.buildActivity(LoginActivity.class).create().get();
        nameEt = activity.findViewById(R.id.editText);
        passwordEt = activity.findViewById(R.id.editText2);
        button = activity.findViewById(R.id.button9);
    }

    /**
     * 测试登录成功，跳转下一页
     */
    @Test
    public void loginSuccess() {
        nameEt.setText("liuxuhui");
        passwordEt.setText("123456");
        button.performClick();
        //Activity跳转的测试
        Intent expectedIntent = new Intent(activity, MainActivity.class);
//        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actual = shadowActivity.getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    /**
     * 测试登录失败，吐司提示
     */
    @Test
    public void loginFail(){
        nameEt.setText("Tony");
        passwordEt.setText("123456");
        button.performClick();
        //Toast测试
        assertEquals("登录失败", ShadowToast.getTextOfLatestToast());
    }

    /**
     * Dialog测试
     */
    public void testDialog(){
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
    }
}
