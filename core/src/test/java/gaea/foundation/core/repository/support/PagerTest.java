package gaea.foundation.core.repository.support;

import org.junit.Assert;
import org.junit.Test;

/**
 * 分页的测试类
 *
 * @author wuhy
 */
public class PagerTest {

    @Test
    public void testPager() {
        Pager pager = new Pager(0, 10, 100, null);
        testPagerInner(pager, "传入正确的值", 0, 10, 100L, 10L, false, true);
        pager = new Pager(-1, 9, 100, null);
        testPagerInner(pager, "传入不正确的当前页", 0, 9, 100L, 12L, false, true);
        pager = new Pager(2, -1, 59, null);
        testPagerInner(pager, "传入不正确的每页记录数", 2, 10, 59L, 6L, true, true);
        pager = new Pager(3, 10, -10, null);
        testPagerInner(pager, "传入不正确的总记录数", 3, 10, -1L, 0L, true, false);
        pager = new Pager(5, 20, 101L, null);
        testPagerInner(pager, "传入边界值", 5, 20, 101L, 6L, true, true);
        pager = new Pager(6, 20, 101L, null);
        testPagerInner(pager, "传入边界值", 6, 20, 101L, 6L, true, false);
    }

    @Test
    public void testGetStartOfPage() {
        Assert.assertEquals("传入正确的值，返回的每页起始位置与比较值不一致", Pager.getStartOfPage(0), 0);
        Assert.assertEquals("传入正确的值，返回的每页起始位置与比较值不一致", Pager.getStartOfPage(10), 100);
        Assert.assertEquals("传入每页记录数，返回的每页起始位置与比较值不一致", Pager.getStartOfPage(2, 20), 40);
        Assert.assertEquals("传入不正确的当前页，返回的每页起始位置与比较值不一致", Pager.getStartOfPage(-1), 0);
        Assert.assertEquals("传入不正确的每页记录数，返回的每页起始位置与比较值不一致", Pager.getStartOfPage(1, -10), 10);
    }

    private void testPagerInner(Pager pager, String preMessage,
                                int pageNo, int pageSize, long totalRecordCount,
                                long totalPageCount, boolean hasPreviousPage, boolean hasNextPage) {
        Assert.assertEquals(preMessage + "，返回的当前页与比较值不一致", pager.getPageNo(), pageNo);
        Assert.assertEquals(preMessage + "，返回的每页记录条数与比较值不一致", pager.getPageSize(), pageSize);
        Assert.assertEquals(preMessage + "，返回的总条数与比较值不一致", pager.getTotalRecordCount(), totalRecordCount);
        Assert.assertEquals(preMessage + "，返回的总页数与比较值不一致", pager.getTotalPageCount(), totalPageCount);
        Assert.assertEquals(preMessage + "，返回是否有上页与比较值不一致", pager.hasPreviousPage(), hasPreviousPage);
        Assert.assertEquals(preMessage + "，返回是否有下页与比较值不一致", pager.hasNextPage(), hasNextPage);
    }
}
