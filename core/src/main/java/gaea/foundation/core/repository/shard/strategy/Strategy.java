package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardParameter;

/**
 * 定义分库分表策略接口
 *
 * @author wuhy
 */
public interface Strategy {
    /**
     * 获取分表标识
     *
     * @param shardParameter 参数
     * @return
     */
    PartitionInfo getPartitionInfo(ShardParameter shardParameter);
}
