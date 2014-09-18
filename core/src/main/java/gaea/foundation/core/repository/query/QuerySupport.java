package gaea.foundation.core.repository.query;


import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardDataRegister;
import gaea.foundation.core.repository.shard.ShardInfo;
import gaea.foundation.core.repository.shard.ShardParameter;
import gaea.foundation.core.repository.support.Pager;
import gaea.foundation.core.repository.query.criterion.Criterion;
import gaea.foundation.core.repository.query.criterion.Order;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class QuerySupport {
    /**
     * 规则条件约束列表
     */
    List<Criterion> criterions = new ArrayList<Criterion>();
    /**
     * 排序
     */
    List<Order> orders = new ArrayList<Order>();
    /**
     * 分区分表参数
     */
    List<PartitionInfo> partitionInfos = new ArrayList<PartitionInfo>();
    /**
     * 分页
     */
    Pager pager = null;

    public static QuerySupport createQuery() {
        return new QuerySupport();
    }

    public QuerySupport add(Criterion criterion) {
        this.criterions.add(criterion);
        return this;
    }

    public QuerySupport addRange(Criterion... criterions) {
        if (criterions != null && criterions.length != 0) {
            for (int i = 0; i < criterions.length; i++) {
                this.criterions.add(criterions[i]);
            }
        }
        return this;
    }

    public QuerySupport addRange(Collection<Criterion> criterions) {
        this.criterions.addAll(criterions);
        return this;
    }

    public QuerySupport addOrder(Order order) {
        this.orders.add(order);
        return this;
    }

    public QuerySupport addOrderRange(Order... orders) {
        if (orders != null && orders.length != 0) {
            for (int i = 0; i < orders.length; i++) {
                this.orders.add(orders[i]);
            }
        }
        return this;
    }

    public QuerySupport addOrderRange(Collection<Order> orders) {
        this.orders.addAll(orders);
        return this;
    }

    public QuerySupport addShard(Class<? extends EntityObject> entityClass, ShardParameter parameter) {
        Assert.notNull(entityClass);
        Assert.notNull(parameter);
        ShardInfo shardInfo = new ShardInfo(entityClass);
        if (shardInfo.isShardSupport()) {
            PartitionInfo partitionInfo = shardInfo.getStrategy().getPartitionInfo(parameter);
            //ShardDataRegister.register(partitionInfo);
            this.partitionInfos.add(partitionInfo);
        }
        return this;
    }

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<PartitionInfo> getPartitionInfos() {
        return partitionInfos;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
