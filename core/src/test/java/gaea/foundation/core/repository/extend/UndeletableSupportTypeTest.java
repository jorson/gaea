package gaea.foundation.core.repository.extend;

import gaea.foundation.core.repository.extend.support.UndeletableSupportType;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @author wuhy
 */
public class UndeletableSupportTypeTest {
    @Test
    public void testGetTypeInstance() {
        Assert.assertEquals("传入整型，返回值与比较值不一致", UndeletableSupportType.getTypeInstance("int", "1"), 1);
        Assert.assertEquals("传入字符型，返回值与比较值不一致", UndeletableSupportType.getTypeInstance("string", "yes"), "yes");
        Assert.assertEquals("传入布尔型，返回值与比较值不一致", UndeletableSupportType.getTypeInstance("boolean", "true"), true);
        Assert.assertEquals("传入长整型，返回值与比较值不一致", UndeletableSupportType.getTypeInstance("long", "1"), 1L);
    }
}
