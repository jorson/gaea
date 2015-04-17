package com.nd.gaea.utils;

import com.nd.gaea.utils.object.TestObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.*;

/**
 * 请在这里输入说明
 *
 * @author bifeng.liu
 */
public class ObjectUtilsTest {
    private TestObject testObject = null;
    private String testObjectString = null;

    @Before
    public void init() {
        testObject = getTestObject();
        testObjectString = getTestObjectJsonString();
    }

    public static TestObject getTestObject() {
        TestObject testObject = new TestObject();
        testObject.setId(100);
        testObject.setName("测试");
        Date birthday = DateUtils.parse("1999-04-03", "yyyy-MM-dd");
        testObject.setBirthday(birthday);
        List<Object> hobby = new ArrayList<Object>();
        hobby.add("hobby_01");
        hobby.add("爱好");
        //Inner Array
        hobby.add(new String[]{"数组1", "数组2"});
        // Inner Properties
        Properties prop = new Properties();
        prop.put("prop_01", "propvalue_01");
        prop.put("prop_02", "propvalue_02");
        hobby.add(prop);
        testObject.setHobby(hobby);
        testObject.setProjects(new String[]{"project", "项目"});
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("attr_01", "属性01");
        attr.put("attr_02", "属性02");
        // Inner Object
        TestObject innerTestObject = new TestObject();
        innerTestObject.setName("ccccc");
        innerTestObject.setBirthday(DateUtils.parse("2011-11-21", "yyyy-MM-dd"));
        attr.put("innerObject", innerTestObject);
        // Inner Map
        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put("Key_01", "1111");
        innerMap.put("Key_02", "cccc");
        innerMap.put("ddd", null);
        attr.put("innerMap", innerMap);
        testObject.setAttributes(attr);
        return testObject;
    }

    public static String getTestObjectJsonString() {
        return new StringBuilder()
                .append("{\"id\":100,\"name\":\"测试\",\"birthday\":\"1999-04-03 00:00:00\"," +
                        "\"hobby\":[\"hobby_01\",\"爱好\",[\"数组1\",\"数组2\"],{\"prop_02\":\"propvalue_02\",\"prop_01\":\"propvalue_01\"}]," +
                        "\"projects\":[\"project\",\"项目\"],\"attributes\":{\"attr_02\":\"属性02\"," +
                        "\"innerObject\":{\"id\":0,\"name\":\"ccccc\",\"birthday\":\"2011-11-21 00:00:00\"" +
                        ",\"hobby\":null,\"projects\":null,\"attributes\":null}," +
                        "\"attr_01\":\"属性01\",\"innerMap\":{\"Key_01\":\"1111\",\"Key_02\":\"cccc\",\"ddd\":null}}}").toString();
    }

    /**
     * 简单对象转换
     * 日期对象转换
     */
    @Test
    public void testSimpleToJson() {
        String str = "测试";
        int index = 100;
        String strDateTime = "2014-01-01 22:11:23";
        String strDate = "2014-01-01";
        Date date = DateUtils.parse(strDateTime, "yyyy-MM-dd HH:mm:ss");
        Object[] array = new Object[]{date, new java.sql.Date(date.getTime()), new Timestamp(date.getTime())};
        Assert.assertEquals("转换简单类(String)的对象，返回值与比较值不一致", ObjectUtils.toJson(str), "\"测试\"");
        Assert.assertEquals("转换简单类(Int)的对象，返回值与比较值不一致", ObjectUtils.toJson(index), "100");
        Assert.assertEquals("转换简单类(Date)的对象，返回值与比较值不一致", ObjectUtils.toJson(array), "[\"" + strDateTime + "\",\"" + strDate + "\",\"" + strDateTime + "\"]");
        // 嵌套对象转换
        // 复杂对象转换
    }

    /**
     * Map对象转换
     * Array对象转换
     * List对象转换
     */
    @Test
    public void testMapToJson() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("str", "测试");
        map.put("int", 10);
        map.put("long", 1000L);
        map.put("bool", true);
        map.put("double", 1.1);
        map.put("object", testObject);
//       / System.out.println(ObjectUtils.toJson(map));
        Assert.assertEquals("转换Map类的对象，返回值与比较值不一致", ObjectUtils.toJson(map), "{\"str\":\"测试\",\"int\":10,\"long\":1000,\"bool\":true,\"double\":1.1,\"object\":" +
                testObjectString + "}");

        Object[] array = new Object[]{"测试", 10, 1000L, false, 1.1, "'=><&;:#$%^*()!~|}{_-+[]\"?", testObject};
        String jsonStr = ObjectUtils.toJson(array);
        String compStr = "[\"测试\",10,1000,false,1.1,\"'=><&;:#$%^*()!~|}{_-+[]\\\"?\"," +
                testObjectString + "]";
        //System.out.println(jsonStr);
        //System.out.println(compStr);
        Assert.assertEquals("转换Array的对象，返回值与比较值不一致", compStr, jsonStr);
        List list = ArrayUtils.asList(array);
        Assert.assertEquals("转换List的对象，返回值与比较值不一致", ObjectUtils.toJson(list), "[\"测试\",10,1000,false,1.1,\"'=><&;:#$%^*()!~|}{_-+[]\\\"?\"," +
                testObjectString + "]");
    }

    /**
     * 简单对象转换
     * 日期对象转换
     */
    @Test
    public void testFromJson() {
        String strDate = "2014-01-01 22:11:23";
        Date date = DateUtils.parse(strDate, "yyyy-MM-dd HH:mm:ss");
        Date[] array = ObjectUtils.fromJson("[\"" + strDate + "\"]", Date[].class);
        Assert.assertEquals("从字符串转换简单类(Date)对象，返回值与比较值不一致", array[0], date);
    }

    /**
     * Map对象转换
     * Array对象转换
     * List对象转换
     * <p/>
     * 注：Map、List、Array转换回来后类型会变化，所以不好做精确比对，先用size来比较
     */
    @Test
    public void testMapFromJson() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("str", "测试");
        map.put("int", 10);
        map.put("long", 1000L);
        map.put("bool", true);
        map.put("double", 1.1);
        map.put("object", testObject);
        Map<String, Object> doMap = ObjectUtils.fromJson(ObjectUtils.toJson(map), LinkedHashMap.class);
        Assert.assertEquals("从字符串转换Map类的对象，返回值与比较值不一致", map.size(), doMap.size());

        Object[] array = new Object[]{"测试", 10, 1000L, false, 1.1, testObject};
        Object[] doArray = ObjectUtils.fromJson(ObjectUtils.toJson(array), Object[].class);
        Assert.assertEquals("从字符串转换Array的对象，返回值与比较值不一致", array.length, doArray.length);
        List list = ArrayUtils.asList(array);
        List doList = ObjectUtils.fromJson(ObjectUtils.toJson(list), ArrayList.class);
        Assert.assertEquals("从字符串转换List的对象，返回值与比较值不一致", list.size(), doList.size());
    }

    @Test
    public void testObjectToJson() {
        Assert.assertEquals("从对象转换成字符串，返回值与比较值不一致", ObjectUtils.toJson(testObject), testObjectString);
        TestObject to = ObjectUtils.fromJson(testObjectString, TestObject.class);

        Assert.assertEquals("从字符串转换成对象，返回值与比较值不一致", to.getName(), "测试");
    }

    @Test
    public void testCustomDateTimeFromToJson() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String strDate = "2014-01-01 22:11:23";
        Date normalDate = DateUtils.parse(strDate, "yyyy-MM-dd HH:mm:ss");
        //com.nd.gaea.core.type.StrictDate customDate = new com.nd.gaea.core.type.StrictDate(normalDate);
        //StrictTime customTime = new StrictTime(normalDate);
        map.put("normalDate", normalDate);
        map.put("long", new Long(100));
        map.put("aa", null);
        //map.put("customDate", customDate);
        //map.put("customTime", customTime);
        String jsonStr = ObjectUtils.toJson(map);
        System.out.println(jsonStr);
    }

}
