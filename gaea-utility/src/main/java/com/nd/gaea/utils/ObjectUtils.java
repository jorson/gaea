package com.nd.gaea.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nd.gaea.utils.gson.DefaultDateTypeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对象操作的工具类
 * <p/>
 * 包括对象的序列化，JSON转换，XML转换等
 *
 * @author bifeng.liu
 */
public class ObjectUtils {

    /**
     * 获取Gson对象
     *
     * @return
     */
    private static Gson getGson() {
        // 类处理器
        Map<Type, Object> typeAdapters = new HashMap<Type, Object>();
        typeAdapters.put(Date.class, new DefaultDateTypeAdapter());
        typeAdapters.put(java.sql.Date.class, new DefaultDateTypeAdapter("yyyy-MM-dd"));
        typeAdapters.put(Time.class, new DefaultDateTypeAdapter("HH:mm:ss"));
        typeAdapters.put(Timestamp.class, new DefaultDateTypeAdapter());

        final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().serializeNulls();
        if (typeAdapters != null && !typeAdapters.isEmpty()) {
            for (Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
                builder.registerTypeAdapter(entry.getKey(), entry.getValue());
            }
        }
        return builder.create();
    }

    /**
     * 把对象转换成JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        // 类处理器
        Map<Type, Object> typeAdapters = new HashMap<Type, Object>();
        typeAdapters.put(Date.class, new DefaultDateTypeAdapter());
        typeAdapters.put(java.sql.Date.class, new DefaultDateTypeAdapter("yyyy-MM-dd"));
        typeAdapters.put(Time.class, new DefaultDateTypeAdapter("HH:mm:ss"));
        typeAdapters.put(Timestamp.class, new DefaultDateTypeAdapter());
        // 子类处理器
        Map<Class<?>, Object> hierarchyTypeAdapters = new HashMap<Class<?>, Object>();
        return toJson(obj, typeAdapters);
    }

    /**
     * 把对象转换成JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj, Map<Type, Object> typeAdapters) {
        final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().serializeNulls();
        if (typeAdapters != null && !typeAdapters.isEmpty()) {
            for (Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
                builder.registerTypeAdapter(entry.getKey(), entry.getValue());
            }
        }
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param str   json格式字符串
     * @param clazz 对象class
     * @return
     */
    public static <T> T fromJson(String str, Class<T> clazz) {
        return fromJson(str, clazz, false);
    }

    /**
     * JSON字符串转换成对象
     *
     * @param str       json格式字符串
     * @param clazz     对象class
     * @param isProcess 是否处理数据（key首字母、下划线处理）
     * @return
     */
    public static <T> T fromJson(String str, Class<T> clazz, boolean isProcess) {
        if (isProcess) {
            str = processJson(str);
        }
        // 类处理器
        Map<Type, Object> typeAdapters = new HashMap<Type, Object>();
        typeAdapters.put(Date.class, new DefaultDateTypeAdapter());
        typeAdapters.put(java.sql.Date.class, new DefaultDateTypeAdapter("yyyy-MM-dd"));
        typeAdapters.put(Time.class, new DefaultDateTypeAdapter("HH:mm:ss"));
        typeAdapters.put(Timestamp.class, new DefaultDateTypeAdapter());
        return fromJson(str, clazz, typeAdapters);
    }


    /**
     * 把对象转换成JSON字符串
     *
     * @param str
     * @param clazz
     * @param typeAdapters
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String str, Class<T> clazz, Map<Type, Object> typeAdapters) {
        final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
        if (typeAdapters != null && !typeAdapters.isEmpty()) {
            for (Map.Entry<Type, Object> entry : typeAdapters.entrySet()) {
                builder.registerTypeAdapter(entry.getKey(), entry.getValue());
            }
        }
        Gson gson = builder.create();
        return gson.fromJson(str, clazz);
    }


    /**
     * 把对象转换成JSON字符串，支持各种泛型
     * <p/>
     * e.g. <code>ObjectUtils.fromJson(json, new TypeToken<Foo<Bar>>() {}.getType());</code>
     *
     * @param json      JSON字符串
     * @param typeToken 类型Token
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        Gson gson = getGson();
        return (T) gson.fromJson(json, typeToken.getType());
    }


    /**
     * 处理json数据首字母小写、驼峰命名
     *
     * @param jsonInput json数据
     * @return string 处理以后的字符串
     */
    public static String processJson(String jsonInput) {
        String originalInput = jsonInput;
        StringBuilder inputStr = new StringBuilder(jsonInput);
        String regex = "\"(\\w+)\":";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputStr);
        List<String> result = new ArrayList<String>();
        String valueName = null;
        while (m.find()) {
            valueName = m.group(1);
            // 首字母小写 或者 以下划线分割的命名
            if (Character.isUpperCase(valueName.charAt(0)) || valueName.indexOf("_") != -1) {
                String newValueName = StringUtils.toLowerFirstLetter(valueName); // 首字母小写
                newValueName = StringUtils.toCamelCase(newValueName); // 下划线命名改为驼峰命名
                String regx1 = "\"" + valueName + "\":";
                String replace = "\"" + newValueName + "\":";
                originalInput = originalInput.replaceAll(regx1, replace);
            }
            result.add(valueName);
            inputStr.delete(0, m.end(0));
            m = p.matcher(inputStr);
        }
        return originalInput;
    }

    /**
     * 把对象序列化成字节数组
     * <p/>
     * 对象一定要实现java.io.Serializable接口
     *
     * @param object
     * @return
     * @throws java.io.IOException
     */
    public static byte[] serialize(Object object) throws IOException {
        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    /**
     * 把字符数据反序列化成对象
     * <p/>
     * 对象一定要实现java.io.Serializable接口
     *
     * @param bytes
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static Object unserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        // 反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
}
