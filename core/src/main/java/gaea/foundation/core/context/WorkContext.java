package gaea.foundation.core.context;

import java.util.*;

/**
 * 提供基于线程的工作上下文
 *
 * @author wuhy
 */
public class WorkContext {
    private static final ThreadLocal<Map<String, Object>> items = new ThreadLocal<Map<String, Object>>();

    /**
     * 取得保存的内容，如果不存在则直接返回null
     *
     * @param key 键
     * @return
     */
    public static Object getValue(String key) {
        Map<String, Object> data = items.get();
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
        Map<String, Object> data = items.get();
        if (data == null) {
            data = new HashMap<String, Object>();
            items.set(data);
        }
        data.put(key, value);
    }
}
