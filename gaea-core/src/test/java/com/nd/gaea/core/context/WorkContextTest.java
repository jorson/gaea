package com.nd.gaea.core.context;

import org.junit.Assert;
import org.junit.Test;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class WorkContextTest {

    @Test
    public void testGetSetValue() {
        for (int i = 0; i < 5; i++) {
            final int val = i;
            new Thread(new Runnable() {
                public void run() {
                    int count = 0;
                    WorkContext.setValue("test", val);
                    while (count < 5) {
                        Assert.assertEquals("取得线程对象中的值与比较值不一致", WorkContext.getValue("test"), val);
                        sleepThread(100);
                        count++;
                    }
                }
            }).start();
        }
        // 如果这里不加，会使得线程还没有执行完成就直接退出
        sleepThread(1000);
    }

    @Test
    public void testClear() {
        WorkContext.setValue("test", 1111);
        WorkContext.clear();
        Assert.assertNull("清空上下文信息后，取得设置的值不为NULL",WorkContext.getValue("test"));
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
