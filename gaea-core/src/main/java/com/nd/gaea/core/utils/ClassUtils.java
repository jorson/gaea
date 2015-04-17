package com.nd.gaea.core.utils;

import com.nd.gaea.core.exception.BaseRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 类操作工具类
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    private static Log logger = LogFactory.getLog(ClassUtils.class);

    private static final String LOADCLASS_ERROR_MESSAGE = "Unable to load class {0}. Initial cause was {1}";

    /**
     * 私有化构造函数，不允许实例化该类
     */
    private ClassUtils() {
    }

    /**
     * 取得标准的<code>ClassLoader</code>
     *
     * @return 线程上下文的类载入器
     */
    public static ClassLoader getStandardClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 备用的ClassLoader，当线程上下文的ClassLoader无法工作时使用
     *
     * @return the <code>ClassUtils.class.getClassLoader();</code>
     */
    public static ClassLoader getFallbackClassLoader() {
        return ClassUtils.class.getClassLoader();
    }

    /**
     * 使用无参数的构造函数，创建一个类的实例，使用标准的ClassLoader
     *
     * @param clazz 类
     * @return 对象
     * @throws com.nd.gaea.core.exception.BaseRuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object createNewInstance(Class clazz) throws BaseRuntimeException {
        return createNewInstance(clazz, new Class[0], new Object[0]);
    }

    /**
     * 使用无参数的构造函数，创建一个类的实例，使用标准的ClassLoader
     *
     * @param className 全路径的类名
     * @return 对象
     * @throws com.nd.gaea.core.exception.BaseRuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object createNewInstance(String className) throws BaseRuntimeException {
        return createNewInstance(className, new Class[0], new Object[0]);
    }

    /**
     * 创建一个类的实例，使用标准的ClassLoader
     *
     * @param clazz    类
     * @param argTypes 构造函数的类型
     * @param args     传构造函数的值
     * @return 对象
     * @throws BaseRuntimeException 如果没有创建成功，则抛出异常
     */
    public static <T> T createNewInstance(Class<T> clazz, Class[] argTypes, Object[] args) throws BaseRuntimeException {
        Object newInstance;
        try {
            Constructor constructor = clazz.getConstructor(argTypes);
            newInstance = constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new BaseRuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, clazz.getName(), e.getCause().getMessage()), e.getCause());
        } catch (Exception e) {
            throw new BaseRuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, clazz.getName(), e.getMessage()), e);
        }
        return (T) newInstance;
    }

    /**
     * 创建一个类的实例，使用标准的ClassLoader
     *
     * @param className 全路径的类名
     * @param argTypes  构造函数的类型
     * @param args      传构造函数的值
     * @return 对象
     * @throws BaseRuntimeException 如果没有创建成功，则抛出异常
     */
    public static Object createNewInstance(String className, Class[] argTypes, Object[] args) throws BaseRuntimeException {
        Class clazz;
        try {
            clazz = loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new BaseRuntimeException(MessageUtils.format(LOADCLASS_ERROR_MESSAGE, className, e.getMessage()), e);
        }
        return createNewInstance(clazz, argTypes, args);
    }

    /**
     * 根据类名取得类
     *
     * @param className 全路径的类名
     * @return 类
     * @throws ClassNotFoundException 如果类没有找到，则抛出异常
     */
    public static Class loadClass(String className) throws ClassNotFoundException {
        Class clazz;
        try {
            clazz = Class.forName(className, true, getStandardClassLoader());
        } catch (ClassNotFoundException e) {
            //try fallback
            clazz = Class.forName(className, true, getFallbackClassLoader());
        }
        return clazz;
    }
}
