package com.nd.gaea.utils.page;

/**
 * 可分页的接口对象
 *
 * @author johnny
 */
public interface Paginable 
{

    public int getTotalCount();

    /**
     * 获得查询的最大数值
     * 获得分页数量
     * 获得每页显示的数量
     *
     * 
     */
    public int getTotalPage();

    public int getPageSize();

    public int getPageNo();

    /**
     * 判断是否是第一页
     *
     * @return true 如果是第一页则返回true.
     */
    public boolean isFirstPage();

    /**
     * 判断是否是最后一页
     *
     * @return 如果是最后一页，返回true
     */
    public boolean isLastPage();

    /**
     * 获得下一页，如果是最后一页，就只返回最后一页
     *
     * @return 页码
     */
    public int getNextPage();

    /**
     *
     * 上一页
     *
     * @return 上一页
     */
    public int getPrePage();
}
