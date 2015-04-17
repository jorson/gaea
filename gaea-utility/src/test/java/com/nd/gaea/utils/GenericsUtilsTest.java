package com.nd.gaea.utils;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * Created by ND on 14-4-29.
 */
public class GenericsUtilsTest {

    @Test
    public void testGetSuperClassGenricTypes() {
        Type[] types = GenericsUtils.getSuperClassGenricTypes(RealizeClass.class);
        Assert.assertTrue("父类中只有一个泛型，返回类型数组不正确", types.length == 1 && types[0] == BaseObject.class);
        types = GenericsUtils.getSuperClassGenricTypes(SubMultiRealizeClass.class);
        Assert.assertTrue("父类中有两个泛型，返回类型数组不正确", types.length == 2 && types[0] == BaseObject.class && types[1] == Object.class);
        types = GenericsUtils.getSuperClassGenricTypes(OperateClass.class);
        Assert.assertTrue("父类中有一个已具化的泛型一个未具化的泛型，返回类型数组不正确", types.length == 2 && types[1] == Timestamp.class);
        types = GenericsUtils.getSuperClassGenricTypes(BaseObject.class);
        Assert.assertTrue("父类中没有泛型，返回类型数组不正确", types.length == 0);
        types = GenericsUtils.getSuperClassGenricTypes(NoRealizeClass.class);
        Assert.assertTrue("父类中有泛型但都没有具化，返回类型数组不正确", types.length == 2);
    }

    @Test
    public void testGetSuperClassGenricType() {
        Assert.assertTrue("父类中只有一个泛型，返回类型不正确", GenericsUtils.getSuperClassGenricType(RealizeClass.class) == BaseObject.class);
        Assert.assertTrue("父类中有两个泛型，返回类型不正确", GenericsUtils.getSuperClassGenricType(SubMultiRealizeClass.class) == BaseObject.class);
        Assert.assertTrue("父类中有一个已具化的泛型一个未具化的泛型，返回类型不正确", GenericsUtils.getSuperClassGenricType(OperateClass.class, 1) == Timestamp.class);
        Assert.assertTrue("父类中有一个已具化的泛型一个未具化的泛型，返回类型(Object)不正确", GenericsUtils.getSuperClassGenricType(OperateClass.class, 0) == Object.class);
        Assert.assertTrue("父类中没有泛型，返回类型(Object)不正确", GenericsUtils.getSuperClassGenricType(BaseObject.class) == Object.class);
        Assert.assertTrue("父类中只有一个泛型且传入索引为2，返回类型不正确", GenericsUtils.getSuperClassGenricType(RealizeClass.class, 1) == Object.class);
    }

    interface IBaseObject {

    }

    class BaseObject implements IBaseObject {

    }

    interface IOperateClass<T extends IBaseObject, K> {
        public void test(T t, K k);
    }

    class BaseOperateClass<T extends IBaseObject, K> implements IOperateClass<T, K> {
        public void test(T t, K k) {

        }
    }

    class NoRealizeClass<T extends IBaseObject, K> extends BaseOperateClass<T, K> {

    }

    class OperateClass<T extends IBaseObject> extends BaseOperateClass<T, Timestamp> {

    }

    class RealizeClass extends OperateClass<BaseObject> {

    }

    class MultiRealizeClass<T extends IBaseObject, K> implements IOperateClass<T, K> {
        public void test(T t, K k) {

        }
    }

    class SubMultiRealizeClass extends MultiRealizeClass<BaseObject, Object> {

    }

    class InterfaceMultiRealizeClass implements IOperateClass<BaseObject, Object> {
        public void test(BaseObject t, Object k) {

        }
    }
}
