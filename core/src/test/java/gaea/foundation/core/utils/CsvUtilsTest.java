package gaea.foundation.core.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV工具类的测试类
 */
public class CsvUtilsTest {
    @Test
    public void testJoin() {
        Assert.assertEquals("传入英文字符串数组，返回的字符串与比较字符串不一致", CsvUtils.join(new String[]{"ccc", "ddd"}), "ccc,ddd");
        Assert.assertEquals("传入包含中文字符串数组，返回的字符串与比较字符串不一致", CsvUtils.join(new String[]{"中文", "测试"}), "中文,测试");
        Assert.assertEquals("传入包含换行字符串数组，返回的字符串与比较字符串不一致", CsvUtils.join(new String[]{"abc\r\nd", "测试"}), "\"abc\r\nd\",测试");
        Assert.assertEquals("传入包含逗号等特殊字符串数组，返回的字符串与比较字符串不一致",
                CsvUtils.join(new String[]{"abc,d", "\"测试", "a\tb"}), "\"abc,d\",\"\"\"测试\",a\tb");
    }

    @Test
    public void testSplit() {
        Assert.assertArrayEquals("传入英文字符串，返回的数组与比较数组不一致", CsvUtils.split("aaa,bbb"), new String[]{"aaa", "bbb"});
        Assert.assertArrayEquals("传入中文字符串，返回的数组与比较数组不一致", CsvUtils.split("中文,测试\r\n"), new String[]{"中文", "测试"});
        Assert.assertArrayEquals("传入包含换行的字符串，返回的数组与比较数组不一致", CsvUtils.split("\"abc\r\n\",测试"), new String[]{"abc\r\n", "测试"});
        Assert.assertArrayEquals("传入逗号等特殊字符的字符串，返回的数组与比较数组不一致",
                CsvUtils.split("\"abc,d\",\"\"\"测试\",a\tb"), new String[]{"abc,d", "\"测试", "a\tb"});
    }


    @Test
    public void testSplitMultiLines() {
        List<String[]> data = CsvUtils.splitMultiLines("id,name\r\n1,lbf\r\n2,刘一\n10,刘三");
        Assert.assertTrue("传入多行字符串，返回的列表不正确", data.size() == 4 && (data.get(1)[1].equals("lbf")));
        data = CsvUtils.splitMultiLines("中文标题,name\r\n1,\"lb\r\nf\"\r\n2,\"刘,\t一\"\n10,刘三\r\n");
        Assert.assertTrue("传入多行包含中文、换行、逗号等特殊字符串，返回的列表不正确",
                data.size() == 4 && (data.get(1)[1].equals("lb\r\nf")) && (data.get(2)[1].equals("刘,\t一")));
        data = CsvUtils.splitMultiLines("这不是一个\"正确,的CSV字符\"串\\\r\naaaaaa");
        Assert.assertTrue("传入不规范的字符串，返回的列表不正确", data.size() == 2 && (data.get(0)[0].equals("这不是一个\"正确")));
    }


    /**
     *
     * @throws java.io.IOException
     */
    //@Test
    public void testWriteRead() throws IOException {
        //整理测试数据
        String path = "/opt/test.csv";  // 该文件会在Java所在的盘符下创建
        List<String[]> datas = new ArrayList<String[]>();
        datas.add(new String[]{"标题一", "标题2", "标题三", "123", "", null});
        datas.add(new String[]{"中文", "eng", "换行\r\na", "特殊字符,\"", "特殊字符2!@#$%^&*(_+)", "", null});
        datas.add(new String[]{""}); //空行
        datas.add(new String[]{"再来一行"});
        datas.add(new String[]{"还来一行", "aaaa", ""});
        Charset charset = Charset.forName("GBK");

        CsvUtils.write(path, datas, charset, true);
        List<String[]> results = CsvUtils.read(path, charset);
        InnertAssert.assertListEquals("写入和读取csv时，写入的与读取的内容不一致", datas, results);
    }

    static class InnertAssert extends Assert {
        public static void assertListEquals(String message, List<String[]> expecteds, List<String[]> actuals) {
            if (expecteds == actuals) {
                return;
            }
            if ((expecteds == null && actuals != null) || (actuals == null && expecteds != null) || actuals.size() != expecteds.size()) {
                fail(message);
            }
            for (int i = 0; i < expecteds.size(); i++) {
                String[] exp = expecteds.get(i);
                String[] act = actuals.get(i);
                if ((exp == null && act != null) || (act == null && exp != null) || exp.length != act.length) {
                    fail(message);
                }
                for (int j = 0; j < act.length; j++) {
                    String a = act[j];
                    String e = exp[j];
                    if (!((e == null && "".equals(a)) || a.equals(e))) {
                        fail(message);
                    }
                }
            }
        }
    }
}
