package com.nd.gaea.core.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象. 包含当记录列表和总条数
 *
 * @author bifeng.liu
 */
public class Pager<T> implements Serializable {
    /**
     * 当前页中存放的记录,类型一般为List
     */
    private List<T> items;

    /**
     * 总记录数
     */
    private int totalCount;


    /**
     * 构造方法，只构造空页.
     * <p/>
     * 总记录数为默认值-1
     */
    public Pager(List<T> items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotalCount() {
        return totalCount;
    }
}