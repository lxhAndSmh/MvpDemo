package com.liu.mvpdemo.mvp.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.liu.mvpdemo.MvpApplication;
import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.mvp.home.MainActivity;
import com.liu.mvpdemo.activity.mvp.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author liuxuhui
 * @date 2019/2/11
 */

@Config(application = MvpApplication.class)
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private EditText nameEt;
    private EditText passwordEt;
    private Button button;
    private Activity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(LoginActivity.class);
        nameEt = activity.findViewById(R.id.editText);
        passwordEt = activity.findViewById(R.id.editText2);
        button = activity.findViewById(R.id.button9);
    }

    @Test
    public void loginSuccess() {
        nameEt.setText("liuxuhui");
        passwordEt.setText("123456");
        button.performClick();
        Intent expectedIntent = new Intent(activity, MainActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    @Test
    public void loginFail(){
        nameEt.setText("Tony");
        passwordEt.setText("123456");
        button.performClick();
    }
}