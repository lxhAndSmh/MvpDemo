package com.liu.mvpdemo.mvp;

import com.liu.mvpdemo.mvp.model.Persion;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author liuxuhui
 * @date 2019/2/14
 */
public class PersionTest {

    @Test
    public void sum() {
        Persion persion = new Persion();
        int num = persion.add(1, 2);
        assertEquals(3, num);
    }
}
