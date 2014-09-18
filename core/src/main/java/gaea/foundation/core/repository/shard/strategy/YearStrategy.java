package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.ShardInfo;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class YearStrategy extends DateStrategy {
    private static final String YEAR_FORMAT = "yyyy";

    public YearStrategy(Class entityClass) {
        super(entityClass);
    }

    public YearStrategy(ShardInfo shardInfo) {
        super(shardInfo);
    }

    @Override
    public String getDateFormat() {
        return YEAR_FORMAT;
    }
}
