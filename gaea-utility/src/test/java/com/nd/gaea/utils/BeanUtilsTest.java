package com.nd.gaea.utils;

import com.nd.gaea.utils.object.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * BeanUtils工具类的测试类
 */
public class BeanUtilsTest {

    private TestObject testObject = null;

    @Before
    public void init() {
        testObject = getTestObject();
    }

    public static TestObject getTestObject() {
        SubTestObject testObject = new SubTestObject();
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
        testObject.setCode("code_01");
        return testObject;
    }


    @Test
    public void testGetDeclaredField() throws NoSuchFieldException {
        Assert.assertNotNull("未取得类存在的公有属性", BeanUtils.getDeclaredField(SubTestObject.class, "code"));
        try {
            BeanUtils.getDeclaredField(SubTestObject.class, "noexist");
            Assert.assertTrue("取得类不存在属性时，没有抛出NoSuchFieldException", false);
        } catch (NoSuchFieldException ex) {
            //正常出错
        }
        Assert.assertNotNull("未取得类存在的私有属性", BeanUtils.getDeclaredField(SubTestObject.class, "privateVariable"));
        Assert.assertNotNull("未取得类中存在的父级公有属性", BeanUtils.getDeclaredField(SubTestObject.class, "name"));
        Assert.assertNotNull("未取得类中存在的祖父级公有属性", BeanUtils.getDeclaredField(GrandsonTestObject.class, "name"));
        Assert.assertNotNull("未取得对象存在的公有属性", BeanUtils.getDeclaredField(new SubTestObject(), "code"));
    }

    @Test
    public void testForceGetProperty() throws NoSuchFieldException {
        Assert.assertEquals("取得对象中存在公有属性的值，返回值与比较值不一致", BeanUtils.forceGetProperty(testObject, "code"), "code_01");
        Assert.assertEquals("取得对象中存在父级公有属性的值，返回值与比较值不一致", BeanUtils.forceGetProperty(testObject, "name"), "测试");
        Assert.assertEquals("取得对象中存在私有属性的值，返回值与比较值不一致", BeanUtils.forceGetProperty(testObject, "privateVariable"), "private");
        try {
            Object obj = BeanUtils.forceGetProperty(testObject, "noexist");
            Assert.assertTrue("取得对象不存在属性时，没有抛出NoSuchFieldException", false);
        } catch (NoSuchFieldException ex) {
            //正常出错
        }
    }

    @Test
    public void testForceSetProperty() throws NoSuchFieldException {
        BeanUtils.forceSetProperty(testObject, "code", "code_02");
        BeanUtils.forceSetProperty(testObject, "id", 1000);
        BeanUtils.forceSetProperty(testObject, "privateVariable", "private_02");
        Assert.assertEquals("设置对象中存在公有属性的值，未设置成功", BeanUtils.forceGetProperty(testObject, "code"), "code_02");
        Assert.assertEquals("设置对象中存在父级公有属性的值，未设置成功", BeanUtils.forceGetProperty(testObject, "id"), 1000L);
        Assert.assertEquals("设置对象中存在私有属性的值，未设置成功", BeanUtils.forceGetProperty(testObject, "privateVariable"), "private_02");
        try {
            BeanUtils.forceSetProperty(testObject, "noexist", "cccc");
            Assert.assertTrue("设置对象不存在属性时，没有抛出NoSuchFieldException", false);
        } catch (NoSuchFieldException ex) {
            //正常出错
        }
    }

    @Test
    public void testGetDeclaredMethod() throws NoSuchMethodException {
        Assert.assertNotNull("未取得类存在的公有方法", BeanUtils.getDeclaredMethod(SubTestObject.class, "doPublicMethod"));
        try {
            BeanUtils.getDeclaredMethod(SubTestObject.class, "noexist");
            Assert.assertTrue("取得类不存在方法时，没有抛出NoSuchMethodException", false);
        } catch (NoSuchMethodException ex) {
            //正常出错
        }
        Assert.assertNotNull("未取得类存在的私有方法", BeanUtils.getDeclaredMethod(SubTestObject.class, "doPrivateMethod"));
        Assert.assertNotNull("未取得类中存在的父级公有方法", BeanUtils.getDeclaredMethod(SubTestObject.class, "doParentMethod"));
        Assert.assertNotNull("未取得类中存在的祖父级公有方法", BeanUtils.getDeclaredMethod(GrandsonTestObject.class, "doParentMethod"));
        Assert.assertNotNull("未取得接口存在的方法", BeanUtils.getDeclaredMethod(ITestObject.class, "doInterface", String.class));
        Assert.assertNotNull("未取得对象存在的公有方法", BeanUtils.getDeclaredMethod(new TestObject(), "doParentMethod"));
        Assert.assertNotNull("未取得类的静态方法", BeanUtils.getDeclaredMethod(StaticClass.class, "getStaticMethod", String.class));
    }

    @Test
    public void testForceInvokeMethod() throws NoSuchMethodException {
        Assert.assertEquals("执行对象存在的公有方法", BeanUtils.forceInvokeMethod(testObject, "doPublicMethod"), "doPublicMethod");
        try {
            BeanUtils.forceInvokeMethod(SubTestObject.class, "noexist");
            Assert.assertTrue("执行对象不存在方法时，没有抛出NoSuchMethodException", false);
        } catch (NoSuchMethodException ex) {
            //正常出错
        }
        Assert.assertEquals("执行对象存在的私有方法", BeanUtils.forceInvokeMethod(testObject, "doPrivateMethod"), "doPrivateMethod");
        Assert.assertEquals("执行对象存在的父级公有方法", BeanUtils.forceInvokeMethod(testObject, "doParentMethod"), "doParentMethod");
        Assert.assertEquals("执行对象存在的祖父级公有方法", BeanUtils.forceInvokeMethod(testObject, "doParentMethod"), "doParentMethod");
        Assert.assertEquals("执行对象存在的接口方法", BeanUtils.forceInvokeMethod(testObject, "doInterface", "test"), "interfacetest");
        try {
            BeanUtils.forceInvokeMethod(SubTestObject.class, "doMethod", testObject);
            Assert.assertTrue("执行对象存在的方法，参数为子类，没有抛出NoSuchMethodException", false);
        } catch (NoSuchMethodException ex) {
            //正常出错
        }
    }
}
