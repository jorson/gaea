package gaea.foundation.core.repository.shard.strategy;

import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.utils.StringUtils;

/**
 * 抽象分库分表策略
 * <p/>
 * 取得类配置分库分表的信息
 *
 * @author wuhy
 */
public abstract class AbstractStrategy implements Strategy {

    protected ShardInfo shardInfo;

    public AbstractStrategy(ShardInfo shardInfo) {
        this.shardInfo = shardInfo;
        checkShardInfo();
    }

    public AbstractStrategy(Class entityClass) {
        shardInfo = new ShardInfo(entityClass);
        checkShardInfo();
    }

    public ShardInfo getShardInfo() {
        return shardInfo;
    }

    /**
     * 分库分表信息配置是否正确
     *
     * @return
     */
    protected boolean checkShardInfo() {
        if (shardInfo == null) {
            throw new IllegalArgumentException("分库分表信息不能为空");
        }
        if (shardInfo.isShardSupport()) {
            if (StringUtils.isEmpty(shardInfo.getFormat())) {
                throw new IllegalArgumentException("分库分表信息中[format]不能为空");
            }
            if (StringUtils.isEmpty(shardInfo.getTable())) {
                throw new IllegalArgumentException("分库分表信息中[table]不能为空");
            }
        }
        return true;
    }
}
