package gaea.foundation.core.repository.shard;

import gaea.foundation.core.repository.shard.strategy.NoShardStrategy;
import gaea.foundation.core.repository.shard.strategy.Strategy;

import java.lang.annotation.*;

/**
 * 分库分表的注解配置
 *
 * @author wuhy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Shard {
    /**
     * 分库名称，默认为空
     *
     * @return
     */
    String shard() default "";

    /**
     * 表名称，默认为空
     *
     * @return
     */
    String table() default "";

    /**
     * 格式，默认为空
     *
     * @return
     */
    String format() default "";

    /**
     * 栏位，默认为ID
     *
     * @return
     */
    String[] fields() default "id";

    /**
     * 使用策略类
     */
    Class<? extends Strategy> strategy() default NoShardStrategy.class;
}
