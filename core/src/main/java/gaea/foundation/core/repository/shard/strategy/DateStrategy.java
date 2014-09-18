package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.config.SystemConfig;
import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.repository.shard.ShardParameter;
import gaea.foundation.core.utils.DateUtils;
import gaea.foundation.core.utils.MessageUtils;

import java.util.Date;

/**
 * 日期分库分表策略的基础类
 *
 * @author wuhy
 */
public abstract class DateStrategy extends AbstractStrategy {

    public DateStrategy(Class entityClass) {
        super(entityClass);
    }

    public DateStrategy(ShardInfo shardInfo) {
        super(shardInfo);
    }

    /**
     * 获取分表标识
     *
     * @param shardParameter 参数
     * @return
     */
    public PartitionInfo getPartitionInfo(ShardParameter shardParameter) {
        Date useDate = new Date(shardParameter.getFirstParameter());
        String tid;
        if (!SystemConfig.Instance.isDevelopment()) {
            tid = DateUtils.format(useDate, getDateFormat());
        } else {
            tid = "00";
        }
        return new PartitionInfo(shardInfo.getTable(), MessageUtils.format(shardInfo.getFormat(), tid));
    }

    /**
     * 取得日期的格式
     *
     * @return
     */
    public abstract String getDateFormat();
}
