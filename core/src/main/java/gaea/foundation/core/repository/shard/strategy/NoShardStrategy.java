package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.repository.shard.ShardParameter;

/**
 * 无分区分表策略
 *
 * @author wuhy
 */
public class NoShardStrategy extends AbstractStrategy {
    public NoShardStrategy(Class entityClass) {
        super(entityClass);
    }

    public NoShardStrategy(ShardInfo shardInfo) {
        super(shardInfo);
    }

    @Override
    public PartitionInfo getPartitionInfo(ShardParameter shardParameter) {
        return null;
    }
}
