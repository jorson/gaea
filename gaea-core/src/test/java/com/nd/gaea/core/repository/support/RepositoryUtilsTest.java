package com.nd.gaea.core.repository.support;

import com.nd.gaea.core.repository.query.criterion.Order;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 仓储工具类的Unit Test
 *
 * @author bifeng.liu
 */
public class RepositoryUtilsTest {


    @Test
    public void testRemoveOrders() {
        String sql = "select * from table where id=0 ";
        String orderBy = "order by id";
        String ret = RepositoryUtils.removeOrders(sql + orderBy);
        Assert.assertEquals("输入一个正确的SQL语句，返回值与比较值不一致", sql, ret);
    }

    @Test
    public void testParseOrders() {
        String orderBy = " startDate desc,endDate asc , doDate a ,";
        List<Order> orders = RepositoryUtils.parseOrders(orderBy);
        System.out.println(orders);
    }

    static class InnertAssert extends Assert {
        public static void assertListEquals(String message, List<Order> expecteds, List<Order> actuals) {
            if (expecteds == actuals) {
                return;
            }
            if ((expecteds == null && actuals != null) || (actuals == null && expecteds != null) || actuals.size() != expecteds.size()) {
                fail(message);
            }
            for (int i = 0; i < expecteds.size(); i++) {
                Order exp = expecteds.get(i);
                Order act = actuals.get(i);
                if ((exp == null && act != null) || (act == null && exp != null) || !exp.equals(act)) {
                    fail(message);
                }
            }
        }
    }
}


