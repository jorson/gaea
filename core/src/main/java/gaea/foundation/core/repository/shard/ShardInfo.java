package gaea.foundation.core.repository.shard;

import gaea.foundation.core.repository.exception.RepositoryException;
import gaea.foundation.core.repository.extend.Undeletable;
import gaea.foundation.core.repository.extend.support.UndeletableSupportType;
import gaea.foundation.core.repository.shard.strategy.Strategy;
import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.utils.ArrayUtils;
import gaea.foundation.core.utils.BeanUtils;
import gaea.foundation.core.utils.ClassUtils;
import gaea.foundation.core.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 装载Entity分库分表信息的内部类.
 *
 * @author wuhy
 */
public class ShardInfo {
    /**
     * Logger对象
     */
    private static Log logger = LogFactory.getLog(ShardInfo.class);
    /**
     * 是否支持分
     */
    private boolean shardSupport = false;
    /**
     * 分库名称，默认为空
     */
    private String shard;

    /**
     * 表名称，默认为空
     */
    private String table;

    /**
     * 格式，默认为空
     */
    private String format;
    /**
     * 栏位
     */
    private Field[] fields;
    /**
     * 使用的策略
     */
    private Strategy strategy;

    /**
     * 实体类
     */
    private Class entityClass;

    public ShardInfo(Class entityClass) {
        this.entityClass = entityClass;
        init(entityClass);
    }

    public boolean isShardSupport() {
        return shardSupport;
    }

    public String getShard() {
        return shard;
    }

    public String getTable() {
        return table;
    }

    public String getFormat() {
        return format;
    }

    public Field[] getFields() {
        return fields;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public ShardParameter getShardParamter(Object entity) {
        if (entity == null || ArrayUtils.isEmpty(fields) || !this.entityClass.isAssignableFrom(entity.getClass())) {
            return null;
        }
        long[] values = new long[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            values[i] = generateLong(entity, field);
        }
        return new ShardParameter(values[0], values.length < 2 ? 0 : values[1]);
    }

    @Override
    public String toString() {
        return "ShardInfo{" +
                "shardSupport=" + shardSupport +
                ", shard='" + shard + '\'' +
                ", table='" + table + '\'' +
                ", format='" + format + '\'' +
                '}';
    }

    /**
     * 初始化数据，判断EntityClass是否有Shard
     * <p/>
     * 如果是则设置相关的值
     *
     * @param entityClass 实体类
     */
    private void init(Class entityClass) {
        // 通过EntityClass的annotation判断entity是否undeletable
        if (entityClass.isAnnotationPresent(Shard.class)) {
            shardSupport = true;
            Shard anno = (Shard) entityClass.getAnnotation(Shard.class);
            format = anno.format();
            shard = anno.shard();
            fields = getFields(entityClass, anno.fields());
            table = anno.table();
            strategy = ClassUtils.createNewInstance(anno.strategy(), new Class[]{ShardInfo.class}, new Object[]{this});
        }
    }

    /**
     * 根据栏位名称取得栏位
     *
     * @param entityClass
     * @param fieldNames
     * @return
     */
    private Field[] getFields(Class entityClass, String[] fieldNames) {
        try {
            Field[] results = new Field[fieldNames.length];
            for (int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                results[i] = BeanUtils.getDeclaredField(entityClass, fieldName);
            }
            return results;
        } catch (NoSuchFieldException ex) {
            throw new RepositoryException("取得分区分表信息时出错，" + ex.getMessage(), ex);
        }
    }

    /**
     * 转换成Long类型
     *
     * @param entity
     * @param field
     * @return
     */
    public static long generateLong(Object entity, Field field) {
        Object obj = null;
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            obj = field.get(entity);
            field.setAccessible(accessible);
        } catch (IllegalAccessException e) {
            logger.warn("取得栏位值出错", e);
        }
        return Utils.generateLong(obj);
    }
}
