package gaea.foundation.core.repository.support;

import gaea.foundation.core.repository.query.criterion.Order;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 请在这里输入说明
 *
 * @author wuhy
 */
public class SqlUtilsTest {


    @Test
    public void testParseOrders() {
        String orderBy = " startDate desc,endDate asc , doDate a ,";
        List<Order> orders = SqlUtils.parseOrders(orderBy);
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


