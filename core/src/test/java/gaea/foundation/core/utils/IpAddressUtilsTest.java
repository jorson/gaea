package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class IpAddressUtilsTest {
    @Test
    public void testIpv4ToLong() {
        Assert.assertEquals("传正确的IP地址，返回数值与比较值不一致", IpAddressUtils.ipv4ToLong("127.0.0.1"), 2130706433L);
        Assert.assertEquals("传正确的IP地址，返回数值与比较值不一致", IpAddressUtils.ipv4ToLong("192.168.206.40"), 3232288296L);
        Assert.assertEquals("传正确的IP地址，返回数值与比较值不一致", IpAddressUtils.ipv4ToLong("121.207.105.15"), 2043635983L);
        Assert.assertEquals("传正确的IP地址，返回数值与比较值不一致", IpAddressUtils.ipv4ToLong("255.255.255.255"), 4294967295L);
        Assert.assertEquals("传正确的IP地址，返回数值与比较值不一致", IpAddressUtils.ipv4ToLong("0.0.0.0"), 0L);
        try {
            long ret = IpAddressUtils.ipv4ToLong("256.0.0.0");
            Assert.assertTrue("传不正确的IP地址，未返回错误信息", false);
        } catch (IllegalArgumentException ex) {

        }
        try {
            long ret = IpAddressUtils.ipv4ToLong("-1.0.0.0");
            Assert.assertTrue("传不正确的IP地址，未返回错误信息", false);
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testLongToIpv4() {
        Assert.assertEquals("传正确的长整型，返回IP地址与比较值不一致", IpAddressUtils.longToIpv4(2130706433L), "127.0.0.1");
        Assert.assertEquals("传正确的长整型，返回IP地址与比较值不一致", IpAddressUtils.longToIpv4(3232288296L), "192.168.206.40");
        Assert.assertEquals("传正确的长整型，返回IP地址与比较值不一致", IpAddressUtils.longToIpv4(2043635983L), "121.207.105.15");
        Assert.assertEquals("传正确的长整型，返回IP地址与比较值不一致", IpAddressUtils.longToIpv4(4294967295L), "255.255.255.255");
        Assert.assertEquals("传正确的长整型，返回IP地址与比较值不一致", IpAddressUtils.longToIpv4(0L), "0.0.0.0");
        try {
            String ret = IpAddressUtils.longToIpv4(4294967296L);
            Assert.assertTrue("传不正确的长整型，未返回错误信息", false);
        } catch (IllegalArgumentException ex) {

        }
        try {
            String ret = IpAddressUtils.longToIpv4(-1L);
            Assert.assertTrue("传不正确的长整型，未返回错误信息", false);
        } catch (IllegalArgumentException ex) {

        }
    }
}
