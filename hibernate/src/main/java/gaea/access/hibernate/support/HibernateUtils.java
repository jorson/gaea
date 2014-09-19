package gaea.access.hibernate.support;

import gaea.foundation.core.repository.support.SqlUtils;
import gaea.foundation.core.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.Type;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Hibernate工具类
 */
public abstract class HibernateUtils extends SqlUtils {

    private static Configuration configuration;

    /**
     * 取得真正的实体对象，去掉代理
     *
     * @param proxy
     * @return
     */
    public static Object resolveRealObject(Object proxy) {
        if (proxy == null) {
            return null;
        }
        if (proxy instanceof HibernateProxy) {
            return ((HibernateProxy) proxy).getHibernateLazyInitializer().getImplementation();
        }
        return proxy;
    }

    /**
     * 设置Query的参数
     *
     * @param queryObject
     * @param parameters
     * @return
     */
    public static void applyParameters(Query queryObject, Map<String, Object> parameters) {
        if (!CollectionUtils.isEmpty(parameters)) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                applyParameter(queryObject, key, value);
            }
        }
    }

    /**
     * 设置Query的参数
     *
     * @param queryObject
     * @param parameters
     * @return
     */
    public static void applyParameters(Query queryObject, Object... parameters) {
        if (null != parameters && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                queryObject.setParameter(i, parameters[i]);
            }
        }
    }

    /**
     * 设置Query的参数
     *
     * @param queryObject
     * @param paramName
     * @param value
     * @return
     */
    public static void applyParameter(Query queryObject, String paramName, Object value) {
        if (value instanceof Collection) {
            queryObject.setParameterList(paramName, (Collection) value);
        } else if (value instanceof Object[]) {
            queryObject.setParameterList(paramName, (Object[]) value);
        } else {
            queryObject.setParameter(paramName, value);
        }
    }

    /**
     * 设置Query的标量
     *
     * @param queryObject
     * @param types       标量类型
     * @return
     */
    public static void applyScalars(SQLQuery queryObject, Map<String, Type> types) {
        if (!CollectionUtils.isEmpty(types)) {
            for (Map.Entry<String, Type> entry : types.entrySet()) {
                queryObject.addScalar(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 设置 Criteria的排序信息
     * 排序信息使用id desc,name,status asc
     *
     * @param criteria
     * @param orderBy
     */
    public static void applyOrders(Criteria criteria, String orderBy) {
        if (!StringUtils.isEmpty(orderBy)) {
            String[] arrOrderyBy = orderBy.split(",");
            for (int i = 0; i < arrOrderyBy.length; i++) {
                String name = arrOrderyBy[i];
                String direct = "";
                int pos = name.indexOf(" ");
                if (pos > -1) {
                    direct = name.substring(pos + 1, name.length());
                    name = name.substring(0, pos);
                }
                if ("desc".equalsIgnoreCase(direct)) {
                    criteria.addOrder(Order.desc(name));
                } else {
                    criteria.addOrder(Order.asc(name));
                }
            }
        }
    }

    /**
     * 判断对象是否是某个类
     */
    public static boolean instanceOf(Object entity, Class clazz) {
        return clazz.isAssignableFrom(Hibernate.getClass(entity));
    }

    /**
     * 通过类，取得表名
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class clazz) {
        synchronized (HibernateUtils.class) {
            PersistentClass pc = configuration.getClassMapping(clazz.getName());
            if (pc == null) {
                configuration = configuration.addClass(clazz);
                pc = configuration.getClassMapping(clazz.getName());
            }
            return pc.getTable().getName();
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        HibernateUtils.configuration = configuration;
    }
}