package com.nd.gaea.utils.page;

/**
 *
 * @author johnny
 */
public class SimplePage implements Paginable
{

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

    public static final int DEF_COUNT = 20;
    /**
     * 每页返回的最大数据量条数
     */
    public static final int DEF_MAX_COUNT = 2000;

    /**
     * 检查当前查询页码的有效性
     *
     * @param pageNo 当前页码
     * @return 如果查询只有一页，或者为空，只返回1，否则返回有效的页码
     */
    public static int cpn(Integer pageNo)
    {
        return (pageNo == null || pageNo < 1) ? 1 : pageNo;
    }

    public SimplePage()
    {
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param totalCount
     */
    public SimplePage(int pageNo, int pageSize, int totalCount)
    {
        setTotalCount(totalCount);
        setPageSize(pageSize);
        setPageNo(pageNo);
        adjustPageNo();
    }

    /**
     * 判断页码的有效范围
     */
    public void adjustPageNo()
    {
        if (pageNo == 1)
        {
            return;
        }
        int tp = getTotalPage();
        if (pageNo > tp)
        {
            pageNo = tp;
        }
    }

    @Override
    public int getPageNo()
    {
        return pageNo;
    }

    @Override
    public int getPageSize()
    {
        return pageSize;
    }

    @Override
    public int getTotalCount()
    {
        return totalCount;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getTotalPage()
    {
        int totalPage = totalCount / pageSize;
        if (totalPage == 0 || totalCount % pageSize != 0)
        {
            totalPage++;
        }
        return totalPage;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isFirstPage()
    {
        return pageNo <= 1;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isLastPage()
    {
        return pageNo >= getTotalPage();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getNextPage()
    {
        if (isLastPage())
        {
            return pageNo;
        } else
        {
            return pageNo + 1;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getPrePage()
    {
        if (isFirstPage())
        {
            return pageNo;
        } else
        {
            return pageNo - 1;
        }
    }

    protected int totalCount = 0;

    protected int pageSize = 20;

    protected int pageNo = 1;

    /**
     * To set total count for a query.
     *
     * @param totalCount
     */
    public void setTotalCount(int totalCount)
    {
        if (totalCount < 0)
        {
            this.totalCount = 0;
        } else
        {
            this.totalCount = totalCount;
        }
    }

    /**
     * 设置每页返回的记录数量，必须大于1，页码数据如果
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize)
    {
        if (pageSize < 1)
        {
            this.pageSize = DEF_COUNT;
        } else
        {
            this.pageSize = pageSize;
        }
    }

    /**
     *
     * Set page number for this query, if given parameter less than 1, adjust it to 1.
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo)
    {
        if (pageNo < 1)
        {
            this.pageNo = 1;
        } else
        {
            this.pageNo = pageNo;
        }
    }
}
