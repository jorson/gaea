package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.ShardInfo;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class MonthStrategy extends DateStrategy {
    private static final String MOUTH_FORMAT = "yyyyMM";

    public MonthStrategy(Class entityClass) {
        super(entityClass);
    }

    public MonthStrategy(ShardInfo shardInfo) {
        super(shardInfo);
    }
    @Override
    public String getDateFormat() {
        return MOUTH_FORMAT;
    }
}
