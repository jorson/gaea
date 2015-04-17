package com.nd.gaea.utils.page;


import java.io.Serializable;
import java.util.List;

/**
 * 正确的分页实现
 *
 * @author johnny
 */
public class Pagination extends SimplePage implements Serializable, Paginable
{

    public Pagination()
    {
    }

    public Pagination(int pageNo, int pageSize, int totalCount)
    {
        super(pageNo, pageSize, totalCount);
    }

    public Pagination(int pageNo, int pageSize, int totalCount, List<?> list)
    {
        super(pageNo, pageSize, totalCount);
        this.list = list;
    }

    public int getFirstResult()
    {
        return (pageNo - 1) * pageSize;
    }

    private List<?> list;

    public List<?> getList()
    {
        return list;
    }

    public void setList(@SuppressWarnings("rawtypes") List list)
    {
        this.list = list;
    }
}
