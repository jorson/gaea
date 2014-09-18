package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.ShardInfo;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class DayStrategy extends DateStrategy {
    private static final String DAY_FORMAT = "yyyyMMdd";

    public DayStrategy(Class entityClass) {
        super(entityClass);
    }
    public DayStrategy(ShardInfo shardInfo) {
        super(shardInfo);
    }

    @Override
    public String getDateFormat() {
        return DAY_FORMAT;
    }
}
