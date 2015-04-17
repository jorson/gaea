package com.nd.gaea.core.repository.support;

import com.nd.gaea.core.repository.query.criterion.Order;
import com.nd.gaea.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 仓储的工具类
 *
 * @author bifeng.liu
 */
public class RepositoryUtils {

    private static final Log LOGGER = LogFactory.getLog(RepositoryUtils.class);

    /**
     * 去除sql的select 子句，未考虑union的情况,用于分页查询.
     * <p/>
     * 可以标注从\/\*end\*\/ 开始截取
     * 如果没有标注\/\*end\*\/ ，那么从第一from开始截取
     * <p/>
     * 如果字符串为空白字符串，则原样返回
     *
     * @param sql SQL语句
     * @return 处理后的SQL语句
     */
    public static String removeSelect(String sql) {
        if (!StringUtils.hasText(sql)) {
            return sql;
        }
        int beginPos = sql.toLowerCase().indexOf("/*end*/");
        if (beginPos == -1) {
            beginPos = sql.toLowerCase().indexOf("from");
        } else {
            beginPos += 7;
        }
        Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
        return sql.substring(beginPos);
    }

    /**
     * 去除sql的orderby 子句，用于分页查询.
     * <p/>
     * 如果字符串为空白字符串，则原样返回
     *
     * @param sql SQL语句
     * @return 处理后的SQL语句
     */
    public static String removeOrders(String sql) {
        if (!StringUtils.hasText(sql)) {
            return sql;
        }
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {  //只替换最后一个
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去除sql的groupby 子句，用于分页查询.
     * <p/>
     * 如果字符串为空白字符串，则原样返回
     *
     * @param sql SQL语句
     * @return 处理后的SQL语句
     */
    public static String removeGroups(String sql) {
        if (!StringUtils.hasText(sql)) {
            return sql;
        }
        Pattern p = Pattern.compile("group\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 解析排序信息
     * <p/>
     * 排序字符串使用逗号分隔，如：id desc,name,status asc，
     * 默认使用升序(asc)排序
     * <p/>
     * 注：该方法不会对栏位验证排序方式，如果排序不为desc、asc(不区分大小写)，则使用默认排序升序(asc)；
     * 如果字符串为空白字符串，则返回空列表
     *
     * @param orderBy 排序字符串
     * @return 排序信息列表
     */
    public static List<Order> parseOrders(String orderBy) {
        List<Order> result = new ArrayList<Order>();
        if (!StringUtils.hasText(orderBy)) {
            return result;
        }
        String[] arrOrderyBy = orderBy.trim().split(",");
        for (int i = 0; i < arrOrderyBy.length; i++) {
            String name = arrOrderyBy[i].trim();
            // 去除空字符串
            if (StringUtils.hasText(name)) {
                String direct = "";
                int pos = name.trim().indexOf(" ");
                if (pos > -1) {
                    direct = name.substring(pos + 1, name.length());
                    name = name.substring(0, pos);
                }
                if ("desc".equalsIgnoreCase(direct.trim())) {
                    result.add(Order.desc(name.trim()));
                } else {
                    result.add(Order.asc(name.trim()));
                }
            }
        }
        return result;
    }

    /**
     * 转义like语句中的
     * <code>'\'</code><code>'_'</code><code>'%'</code>
     * <p/>
     * 将<code>'\'</code>转成sql的<code>'\\'</code>
     * 将<code>'_'</code>转成sql的<code>'\_'</code>
     * 将<code>'%'</code>转成sql的<code>'\%'</code>
     *
     * @param sql SQL语句
     * @return 处理后的SQL语句
     */
    public static String escapeLike(String sql) {
        if (!StringUtils.isEmpty(sql)) {
            sql = sql.replace("\\", "\\\\");
            sql = sql.replace("%", "\\%");
            sql = sql.replace("_", "\\_");
        }
        return sql;
    }
}
