package com.liu.mvpdemo.mvp.login;

import com.liu.mvpdemo.activity.mvp.login.LoginActivity;
import com.liu.mvpdemo.activity.mvp.login.LoginPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author liuxuhui
 * @date 2019/2/14
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    @Mock
    private LoginPresenter presenter;

    @Mock
    private LoginActivity view;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(view);
    }

    @Test
    public void testLogin() {
        presenter.confimUser("liuxuhui", "123456");
        verify(view).loginSuccess();
    }

}
