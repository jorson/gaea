package gaea.foundation.core.repository.support;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 * <p/>
 * 默认的每页记录数为10，当前页的数值从1开始，
 * 总记录数只有在初始化时为-1，后续如果设置一个小于0的数，会自动设置成0
 *
 * @author wuhy
 */
public class Pager implements Serializable {

    /**
     * 空分页信息
     */
    public static final Pager EMPTY_PAGER = new Pager();
    /**
     * 默认每页显示记录数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 每页显示记录数
     */
    private int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 当前页
     */
    private int pageNo;

    /**
     * 当前页中存放的记录,类型一般为List
     */
    private Object data;

    /**
     * 总记录数
     */
    private long totalRecordCount;
    /**
     * 排序ID desc,Name,Status asc
     */
    private String orderBy;

    /**
     * 构造方法，只构造空页.
     * <p/>
     * 总记录数为默认值-1
     */
    public Pager() {
        this(0, DEFAULT_PAGE_SIZE, -1L, new ArrayList());
    }

    /**
     * 默认构造方法.
     *
     * @param pageNo      当前页
     * @param totalRecordCount 数据库中总记录条数
     * @param pageSize    本页容量
     * @param data        本页包含的数据
     */
    public Pager(int pageNo, int pageSize, long totalRecordCount, Object data) {
        this.setPageSize(pageSize);
        this.setPageNo(pageNo);
        this.totalRecordCount = totalRecordCount < -1 ? -1 : totalRecordCount;
        this.data = data;
    }


    public void setPageNo(int pageNo) {
        pageNo = checkPageNo(pageNo);
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        pageSize = checkPageSize(pageSize);
        this.pageSize = pageSize;
    }

    /**
     * @param totalRecordCount
     */
    public void setTotalRecordCount(long totalRecordCount) {
        if (totalRecordCount < 0) {
            totalRecordCount = 0;
        }
        this.totalRecordCount = totalRecordCount;
    }

    /**
     * 取总记录数.
     */
    public long getTotalRecordCount() {
        return this.totalRecordCount;
    }

    public void setResult(Object data) {
        this.data = data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 取总页数.
     */
    public long getTotalPageCount() {
        if (totalRecordCount < 0) return 0L;
        if (totalRecordCount % pageSize == 0) {
            return totalRecordCount / pageSize;
        } else {
            return totalRecordCount / pageSize + 1;
        }
    }

    /**
     * 取每页数据容量.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 取当前页中的记录.
     */
    public Object getResult() {
        return data;
    }

    /**
     * 取该页当前页码,页码从0开始.
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 取排序信息
     */
    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 该页是否有下一页.
     */
    public boolean hasNextPage() {
        return this.getPageNo() < this.getTotalPageCount();
    }

    /**
     * 该页是否有上一页.
     */
    public boolean hasPreviousPage() {
        return this.getPageNo() > 1;
    }

    /**
     * 获取任一页第一条数据在数据集的位置，每页条数使用默认值.
     *
     * @param pageNo 从1开始的页号
     * @see #getStartOfPage(int, int)
     */
    public static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取任一页第一条数据在数据集的位置.
     *
     * @param pageNo   从1开始的页号
     * @param pageSize 每页记录条数
     * @return 该页第一条数据
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        pageNo = checkPageNo(pageNo);
        pageSize = checkPageSize(pageSize);
        //return (pageNo - 1) * pageSize;
        return pageNo  * pageSize;
    }

    /**
     * 检查当前页是否合法，如果不合法直接设置成初始页0
     *
     * @param pageNo
     * @return
     */
    private static int checkPageNo(int pageNo) {
        if (pageNo < 0) {
            pageNo = 0;
        }
        return pageNo;
    }

    /**
     * 检查每页记录数是否合法，如果不合法直接设置成默认值
     *
     * @param pageSize
     * @return
     */
    private static int checkPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static void main(String[] args) {
        Pager pager = new Pager(1, 10, 100, null);
        System.out.println("Page Count:" + pager.getTotalPageCount());
        System.out.println("Total Count:" + pager.getTotalRecordCount());
        System.out.println("hasPreviousPage:" + pager.hasPreviousPage());
        System.out.println("hasNextPage:" + pager.hasNextPage());
        Pager pager1 = new Pager(10, 10, 101, null);
        System.out.println("Page Count:" + pager1.getTotalPageCount());
        System.out.println("Total Count:" + pager1.getTotalRecordCount());
        System.out.println("hasPreviousPage:" + pager1.hasPreviousPage());
        System.out.println("hasNextPage:" + pager1.hasNextPage());
        Pager pager2 = new Pager(11, 10, 101, null);
        System.out.println("Page Count:" + pager2.getTotalPageCount());
        System.out.println("Total Count:" + pager2.getTotalRecordCount());
        System.out.println("hasPreviousPage:" + pager2.hasPreviousPage());
        System.out.println("hasNextPage:" + pager2.hasNextPage());

    }
}