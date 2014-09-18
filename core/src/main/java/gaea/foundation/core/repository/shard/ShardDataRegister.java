package gaea.foundation.core.repository.shard;

import gaea.foundation.core.context.WorkContext;

import java.util.*;

/**
 * 分区分表数据暂存器
 *
 * @author wuhy
 */
public class ShardDataRegister {
    private static final ThreadLocal<List<PartitionInfo>> items = new ThreadLocal<List<PartitionInfo>>();

    /**
     * 取得保存的内容，如果不存在则直接返回null
     *
     * @param key 键
     * @return
     */
    public static List<PartitionInfo> getPartitionInfos() {
        return items.get();
    }

    /**
     * 在当前线程对象中保存值
     *
     * @param key   键
     * @param value 值
     */
    public static boolean register(Collection<PartitionInfo> partitionInfos) {
        List<PartitionInfo> data = items.get();
        if (data == null) {
            data = new ArrayList<PartitionInfo>();
            items.set(data);
        }
        data.addAll(partitionInfos);
        return true;
    }

    /**
     * 在当前线程对象中保存值
     *
     * @param key   键
     * @param value 值
     */
    public static boolean register(PartitionInfo partitionInfo) {
        List<PartitionInfo> data = items.get();
        if (data == null) {
            data = new ArrayList<PartitionInfo>();
            items.set(data);
        }
        data.add(partitionInfo);
        return true;
    }

    /**
     * 清除数据
     */
    public static void clear() {
        items.set(null);
    }

}
