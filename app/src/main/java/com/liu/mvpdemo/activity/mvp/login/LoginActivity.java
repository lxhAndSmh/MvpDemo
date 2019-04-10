package com.liu.mvpdemo.activity.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.activity.mvp.home.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author liuxuhui 
 * @date 2019/2/11
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View{

    @BindView(R.id.editText)
    public EditText nameEt;
    @BindView(R.id.editText2)
    public EditText passwordEt;

    private LoginContract.Presenter mPresenter;
    private LoginContract.Model mModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mModel = new LoginModel();
        mPresenter = new LoginPresenter(this, mModel);
    }

    @OnClick(R.id.button9)
    public void login() {
        mPresenter.confirmUser(nameEt.getText().toString(), passwordEt.getText().toString());
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFailed() {
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
    }
}
