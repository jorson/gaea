package com.nd.gaea.core.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author bifeng.liu
 * @since 2015/2/15.
 */
public class SystemConfigLoaderTest {
    @Test
    public void testStartup() {
        SystemConfigLoader.startup();
        Assert.assertEquals("取得存在的值，返回的字符串与比较字符串不一致", SystemConfig.instance.getProperty("unit.key"), "project值");
        Assert.assertEquals("取得存在的值，返回的字符串与比较字符串不一致", SystemConfig.instance.getProperty("unit.otherkey"), "另一个project core值");
        Assert.assertTrue("取得存在的值，返回的值与比较值不一致", SystemConfig.instance.getBooleanProperty("system.model.development"));
        Assert.assertNull("取得不存在的值，返回的值不返回NULL", SystemConfig.instance.getProperty("unset"));
    }
}
