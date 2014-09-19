package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * ArrayUtils的测试类
 */
public class ArrayUtilsTest {
    /**
     * 测试ArrayUtils.join([]);
     */
    @Test
    public void testJoin() {
        Assert.assertNull("join方法传入null，返回不为null", ArrayUtils.join(null));
        Assert.assertEquals("join方法传入空字符串数组，返回不为空字符串", ArrayUtils.join(new String[]{}), "");
        Assert.assertEquals("join方法传入字符串数组，返回与比较字符串不相同", ArrayUtils.join(new String[]{"a", "b", "c"}), "abc");
        Assert.assertEquals("join方法传入字符串数组中有null和空字符串，返回与比较字符串不相同", ArrayUtils.join(new String[]{null, "", "c"}), "c");
        Assert.assertEquals("join方法传入字符串数组中有中文，返回与比较字符串不相同", ArrayUtils.join(new String[]{"中文", "", "c"}), "中文c");
    }

    /**
     * 测试ArrayUtils.join([],separator);
     */
    @Test
    public void testSeparatorJoin() {
        Assert.assertNull("join separator方法传入null，返回不为null", ArrayUtils.join(null, "-"));
        Assert.assertEquals("join separator方法传入空字符串数组，返回不为空字符串", ArrayUtils.join(new String[]{}, "-"), "");
        Assert.assertEquals("join separator方法传入字符串数组，返回与比较字符串不相同", ArrayUtils.join(new String[]{"a", "b", "c"}, "-"), "a-b-c");
        Assert.assertEquals("join separator方法传入字符串数组中有null和空字符串，返回与比较字符串不相同", ArrayUtils.join(new String[]{null, "", "c"}, "-"), "--c");
        Assert.assertEquals("join separator方法传入字符串数组中有中文，返回与比较字符串不相同", ArrayUtils.join(new String[]{"中文", "", "c"}, "-"), "中文--c");
        Assert.assertEquals("join separator方法传入字符串数组中有特殊字符，返回与比较字符串不相同", ArrayUtils.join(new String[]{"&*()-+", "()!@$", "\\\r\n"}, "-"), "&*()-+-()!@$-\\\r\n");
        Assert.assertEquals("join separator方法传入字符串数组使用中文作为分隔符，返回与比较字符串不相同", ArrayUtils.join(new String[]{"c", "d", "c"}, "中文"), "c中文d中文c");
    }

    /**
     * 测试ArrayUtils.join([],separator,startIndex,length);
     */
    @Test
    public void testSeparatorPositionJoin() {
        Assert.assertNull("join方法传入null和长度，返回不为null", ArrayUtils.join(null, "-", 1, 100));
        Assert.assertEquals("join separator方法传入空字符串数组和长度，返回不为空字符串", ArrayUtils.join(new String[]{}, "-", -1, 100), "");
        Assert.assertEquals("join separator方法传入字符串数组、开始和长度，返回与比较字符串不相同", ArrayUtils.join(new String[]{"a", "b", "c", "d"}, "-", 2, 2), "c-d");
        Assert.assertEquals("join separator方法传入字符串数组、开始和长度，返回与比较字符串不相同", ArrayUtils.join(new String[]{"a", "b", "c", "d"}, "-", 0, 5), "a-b-c-d");
    }


    @Test
    public void testAsList() {
        Assert.assertTrue("asList方法传入null，返回的值不为NULL", ArrayUtils.asList(null).size() == 0);
        Assert.assertTrue("asList方法传入空数组，返回列表的长度不为0", ArrayUtils.asList(new String[]{}).size() == 0);
        Assert.assertTrue("asList方法传入不为空的数组，返回列表的长度为0", ArrayUtils.asList(new String[]{"aa"}).size() > 0);
    }

}
