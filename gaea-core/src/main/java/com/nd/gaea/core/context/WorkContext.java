package com.nd.gaea.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供基于线程的工作上下文
 *
 * @author bifeng.liu
 */
public class WorkContext {
    /**
     * 用来存储内容本地线程对象
     */
    private static final ThreadLocal<Map<String, Object>> itemsHolder = new ThreadLocal<Map<String, Object>>();

    /**
     * 取得保存的内容，如果不存在则直接返回null
     *
     * @param key 键
     * @return
     */
    public static Object getValue(String key) {
        Map<String, Object> data = itemsHolder.get();
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    /**
     * 在当前线程对象中保存值
     *
     * @param key   键
     * @param value 值
     */
    public static void setValue(String key, Object value) {
        Map<String, Object> data = itemsHolder.get();
        if (data == null) {
            data = new HashMap<String, Object>();
            itemsHolder.set(data);
        }
        data.put(key, value);
    }

    /**
     * 清除工作上下文所有内容
     */
    public static void clear() {
        itemsHolder.remove();
    }
}
