package com.liu.mvpdemo.mvp;

import com.liu.mvpdemo.mvp.model.Persion;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author liuxuhui
 * @date 2019/2/14
 */
public class MocitoTest {

    @Test
    public void testList() {
        //创建mock对象
        List<String> mockList = mock(List.class);
        mockList.add("one");
        System.out.println(mockList.get(0));
        mockList.clear();

        //验证有没有调用add()方法和clear()方法
        verify(mockList).add("one");
        verify(mockList).clear();

        //当执行mockList.get(0)时，返回""
        when(mockList.get(0)).thenReturn("one");
        assertEquals("one", mockList.get(0));
        System.out.println("result: " + mockList.get(0));
        //一个mock的对象对所有非void方法都返回默认值：int,long类型方法将返回0，boolean将返回false，对象方法将返回null。
        //如果是spy的对象，spy.get(999)则会抛数组越界的异常。
        System.out.println("result: " + mockList.get(999));
    }

    @Test
    public void testPersion() {
        Persion persion = mock(Persion.class);
        when(persion.add(1, 3)).thenReturn(4);
        int num = persion.add(1, 3);
        assertEquals(4, num);
    }

    @Test
    public void testSpy() {
        Persion persion = new Persion();
        //spy()方法默认调用这个类的真实对象，可以通过when().thenReturn()来指定spy对象的方法的行为，
        //也可以通过doReturn().when()
        Persion spy = Mockito.spy(persion);
        //when(spy.add(1, 3))会导致spy对象的方法被真正执行。如果
        when(spy.add(1, 3)).thenReturn(4);
        int num = spy.add(1, 3);
        assertEquals(4, num);
    }
}
