package gaea.foundation.core.utils;

import gaea.foundation.core.utils.object.EnumObject;
import gaea.foundation.core.utils.object.ResourceType;
import gaea.foundation.core.utils.object.TestObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * ObjectUtils的测试类
 */
public class ObjectUtilsTest {
    private TestObject testObject = null;
    private String testObjectString = null;

    @Before
    public void init() {
        testObject = getTestObject();
        testObjectString = getTestObjectXmlString();
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
        testObject.setHobby(hobby);
        testObject.setProjects(new String[]{"project", "项目"});
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("attr_01", "属性01");
        attr.put("attr_02", "属性02");
        testObject.setAttributes(attr);
        return testObject;
    }


    public static String getTestObjectXmlString() {
        return new StringBuilder()
                .append("<com.huayu.foundation.core.gaea.platform.security.utils.object.TestObject>")
                .append("<id>100</id>")
                .append("<name>测试</name>")
                .append("<birthday>1999-04-03 00:00:00</birthday>")
                .append("<hobby><string>hobby_01</string><string>爱好</string></hobby>").append(
                        "<projects><string>project</string><string>项目</string></projects>").append(
                        "<attributes>").append(
                        "<entry><string>attr_02</string><string>属性02</string></entry>").append(
                        "<entry><string>attr_01</string><string>属性01</string></entry>").append(
                        "</attributes>")
                .append("</com.huayu.foundation.core.gaea.platform.security.utils.object.TestObject>").toString();
    }

    public static String getMapXmlString() {
        return "<map><entry><string>attr_02</string><string>属性02</string></entry>" +
                "<entry><string>attr_01</string><string>属性01</string></entry></map>";
    }

    public static String getListXmlString() {
        return "<list><string>hobby_01</string><string>爱好</string></list>";
    }

    public static String getArrayXmlString() {
        return "<string-array><string>project</string><string>项目</string></string-array>";
    }

    /**
     * 测试ObjectUtils.toXml(Object);
     */
    @Test
    public void testToXml() {
        Assert.assertEquals("传入null，返回值为空", ObjectUtils.toXml(null), "<null/>");
        Assert.assertEquals("传入有效对象，返回值与比较的字符不一样",
                StringUtils.trimAllWhitespace(ObjectUtils.toXml(testObject)),
                StringUtils.trimAllWhitespace(getTestObjectXmlString()));
        Assert.assertEquals("传入Map对象，返回值与比较的字符不一样", StringUtils.trimAllWhitespace(ObjectUtils.toXml(testObject.getAttributes())), getMapXmlString());
        Assert.assertEquals("传入List对象，返回值与比较的字符不一样", StringUtils.trimAllWhitespace(ObjectUtils.toXml(testObject.getHobby())), getListXmlString());
    }

    /**
     * 测试ObjectUtils.fromXml(String);
     */
    @Test
    public void testFromXml() {
        Assert.assertNotNull("传入有效字符串，返回值对象为Null", ObjectUtils.fromXml(testObjectString));
        Assert.assertTrue("传入有效Map字符串，返回值对象不为Map", ObjectUtils.fromXml(getMapXmlString()) instanceof Map);
        Assert.assertTrue("传入有效List字符串，返回值对象不为List", ObjectUtils.fromXml(getListXmlString()) instanceof List);
        Assert.assertTrue("传入有效String[]字符串，返回值对象不为String数组", ObjectUtils.fromXml(getArrayXmlString()) instanceof String[]);

    }

    @Test
    public void testDeepClone() {
        Assert.assertNull("传入null，返回值对象不为Null", ObjectUtils.deepClone(null));
        Assert.assertNotNull("传入有效的对象，返回值对象不为Null", ObjectUtils.deepClone(testObject));
        TestObject tempObject = ObjectUtils.deepClone(testObject);
        TestObject innerTestObject = new TestObject();
        innerTestObject.setName("Inner Object");
        tempObject.getHobby().add(tempObject);
        tempObject.getAttributes().put("attr_03", innerTestObject);
        TestObject to = ObjectUtils.deepClone(tempObject);
        TestObject deInnerTestObject = ((TestObject) to.getHobby().get(2));
        deInnerTestObject.setName("Deep Inner Object");
        Assert.assertTrue("传入内部对象，返回值对象与输入对象一值，不是深拷贝",
                ("Inner Object".equals(innerTestObject.getName()) && deInnerTestObject != innerTestObject));
    }

    @Test
    public void testEnum() {
        System.out.println(ObjectUtils.toJson(ResourceType.VIDEO));
    }

/**
 * 查看输出值
 *
 * @param args
 */
//    public static void main(String[] args) {
//        TestObject testObject = getTestObject();
//        System.out.println(ObjectUtils.toXml(null));
//        System.out.println(ObjectUtils.toXml("dddd"));
//        System.out.println(ObjectUtils.toXml(testObject));
//        System.out.println(ObjectUtils.toXml(testObject.getAttributes()));
//        System.out.println(ObjectUtils.toXml(testObject.getHobby()));
//        System.out.println(ObjectUtils.toXml(testObject.getProjects()));
//    }
}
