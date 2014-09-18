package gaea.foundation.core.cache;

import java.io.Serializable;

/**
 * 用于表示空数据
 *
 * @author wuhy
 */
public class EmptyData implements Serializable, Cloneable {

    public static final EmptyData value = new EmptyData();


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyData;
    }

}
