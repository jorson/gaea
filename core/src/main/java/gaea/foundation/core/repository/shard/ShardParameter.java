package gaea.foundation.core.repository.shard;

import gaea.foundation.core.utils.StringUtils;
import gaea.foundation.core.utils.Utils;

import java.text.ParseException;

/**
 * 分库分表的依赖参数
 * <p/>
 * 目前仅支持2个参数。非long类型的可以先转为long类型，如DateTime.
 *
 * @author wuhy
 */
public class ShardParameter {
    /**
     * 空的分库分库参数
     */
    public static final ShardParameter EMPTY = new ShardParameter(0);

    private long firstParameter;
    private long secondParameter;

    public ShardParameter(long firstParameter) {
        this(firstParameter, 0);
    }

    public ShardParameter(long firstParameter, long secondParameter) {
        this.firstParameter = firstParameter;
        this.secondParameter = secondParameter;
    }

    public long getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(long firstParameter) {
        this.firstParameter = firstParameter;
    }

    public long getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(long secondParameter) {
        this.secondParameter = secondParameter;
    }

    public String toString() {
        return firstParameter + "," + secondParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShardParameter that = (ShardParameter) o;
        if (firstParameter != that.firstParameter) return false;
        if (secondParameter != that.secondParameter) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (firstParameter ^ (firstParameter >>> 32));
        result = 31 * result + (int) (secondParameter ^ (secondParameter >>> 32));
        return result;
    }

    /**
     * 保存两个分库分表参数
     *
     * @param firstParameter
     * @param secondParameter
     * @return
     */
    public static ShardParameter form(long firstParameter, long secondParameter) {
        return new ShardParameter(firstParameter, secondParameter);
    }

    /**
     * 保存单个分库分表参数，第二个参数为0
     *
     * @param firstParameter
     * @return
     */
    public static ShardParameter form(long firstParameter) {
        return new ShardParameter(firstParameter, 0);
    }

    /**
     * 解析字符串，转换成分库分表参数
     * <p/>
     * 字符串必须使用一个逗号分隔的数值，否则返回空参数
     *
     * @param value
     * @return
     */
    public static ShardParameter parse(String value) {
        if (!StringUtils.hasText(value)) {
            return EMPTY;
        }

        String[] vs = value.split(",");
        if (vs.length != 2) {
            return EMPTY;
        }
        try {
            return new ShardParameter(Utils.toLong(vs[0]), Utils.toLong(vs[1]));
        } catch (ParseException ex) {
            return EMPTY;
        }
    }
}
