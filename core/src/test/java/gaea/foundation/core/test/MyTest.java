package gaea.foundation.core.test;

import org.junit.Test;

import java.util.Date;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class MyTest {
    @Test
    public void test() throws Exception {

//        List list = null;
//        Method doMethod = BeanUtils.getDeclaredMethod(Restrictions.class, "eq", String.class, Object.class);
//        Criterion c = (org.hibernate.criterion.Criterion) doMethod.invoke(Restrictions.class, "111", "cccc");
//        System.out.println(c);
    }



    @Test
    public void testUnixDate(){
        System.out.println(new Date().getTime()/1000);
    }
}
