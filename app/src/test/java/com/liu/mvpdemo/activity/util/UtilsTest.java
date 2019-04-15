package com.liu.mvpdemo.activity.util;

import com.liu.mvpdemo.activity.mvp.login.LoginModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author liuxuhui
 * @date 2019/4/15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class UtilsTest {

    /**
     * Mock普通类的静态方法
     * @PrepareForTest(), 注解里写的类是需要mock的final方法所在的类
     */
    @Test
    public void generateNewUUId() {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.generateNewUUId()).thenReturn("FAKE UUID");
        LoginModel loginModel = new LoginModel();
        assertEquals(loginModel.printUUID(), "FAKE UUID");
    }

    /**
     * Mock普通类的私有方法
     * @PrepareForTest(), 注解里写的类是需要mock的final方法所在的类
     * @throws Exception
     */
    @Test
    public void isExist() throws Exception {
        Utils utils = PowerMockito.mock(Utils.class);
        PowerMockito.when(utils.callPrivateMethod()).thenCallRealMethod();
        PowerMockito.when(utils, "isExist").thenReturn(true);
        assertTrue(utils.callPrivateMethod());
    }

    /**
     * Mock普通类的final方法
     * @PrepareForTest(), 注解里写的类是需要mock的final方法所在的类
     */
    @Test
    public void callFinalMethod() {
        Utils utils = PowerMockito.mock(Utils.class);
        LoginModel loginModel = new LoginModel();
        PowerMockito.when(utils.isAlive()).thenReturn(true);
        Assert.assertTrue(loginModel.callFinalMethod(utils));
    }
}