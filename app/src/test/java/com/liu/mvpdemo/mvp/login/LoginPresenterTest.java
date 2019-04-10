package com.liu.mvpdemo.mvp.login;

import com.liu.mvpdemo.activity.mvp.login.LoginActivity;
import com.liu.mvpdemo.activity.mvp.login.LoginModel;
import com.liu.mvpdemo.activity.mvp.login.LoginPresenter;
import com.liu.mvpdemo.activity.mvp.login.NetworCallBack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private LoginModel model;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(view, model);
    }

    /**
     *  verify用来判断方法的执行次数和顺序
     *  Mockito.verify(mockObject, Mockito.times(1)).methodTODO("xiaochuang", "xiaochuang password");
     *  如果要验证一个对象的某个方法被调用了多少次，只需将次数传给Mockito.times()就好。
     */
    @Test
    public void testLogin() {
        presenter.confirmUser("liuxuhui", "123456");
        //这是一个重载方法，相当于verify(view, times(1)).loginSuccess();
        verify(view).loginSuccess();
    }

    /**
     * doAnswer用来判断执行的方法和方法的参数
     */
    @Test
    public void testLoginByNetwork() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                //在answer里判断执行的参数是否为期望的类型或数值
                //invocation.getArguments()获取执行方法所传的参数（这里的uploadUserInfo方法有三个参数）
                Object[] arguments = invocation.getArguments();
                String name = (String) arguments[0];
                String password = (String) arguments[1];
                NetworCallBack callBack = (NetworCallBack) arguments[2];
                if(name.equals("liu") && password.equals("123")) {
                    return true;
                }else {
                    throw new RuntimeException();
                }
            }
        }).when(model).uploadUserInfo(anyString(), anyString(), any(NetworCallBack.class));
        //当model调用uploadUserInfo方法时，也会执行answer里的代码
        presenter.longinByNetwork("liu", "123");
    }
}
