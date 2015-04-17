package com.nd.gaea.repository.hibernate.usertype;

import com.nd.gaea.utils.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Json支持用户类型
 * <p>
 * 在xml可以这样子配置
 * <code>
 * &lt;property name="jsonEntity" column="jsonData"&gt;
 * &lt;type name="JsonUserType"&gt;
 * &lt;param name="clazz"&gt;com.nd.gaea.repository.hibernate.object.JsonEntity&lt;/param&gt;
 * &lt;/type&gt;
 * &lt;/property&gt;
 * </code>
 * </p>
 *
 * @author bifeng.liu
 */
public class JsonUserType implements UserType, DynamicParameterizedType {

    /**
     * 实体的类名，用于转换
     */
    public static final String ENTITYCLASS = "entityClass";

    private Class entityClass = null;

    private static final int[] SQL_TYPES = {Types.VARCHAR};

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return entityClass;
    }

    /**
     * 安全获取值
     * <p/>
     * 如果不为null时，把json字符串转换成实体类型
     *
     * @param resultSet
     * @param names
     * @param session
     * @param owner
     * @return
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);
        Object result = null;
        //如果不为NULL，则转换成实体类
        if (!resultSet.wasNull()) {
            result = ObjectUtils.fromJson(name, entityClass);
        }
        return result;
    }

    /**
     * 安全设置值
     * <p/>
     * 如果不为null时，把实体对象转换成json字符串，保存到数据库中
     *
     * @param preparedStatement
     * @param value
     * @param index
     * @param session
     * @throws HibernateException
     * @throws SQLException
     */
    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, ObjectUtils.toJson(value));
        }
    }

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            entityClass = reader.getReturnedClass();
        } else {
            String entityClassName = (String) parameters.get(ENTITYCLASS);
            try {
                entityClass = ReflectHelper.classForName(entityClassName, this.getClass());
            } catch (ClassNotFoundException exception) {
                throw new HibernateException("Entity class not found", exception);
            }
        }
    }

    /**
     * 深拷贝
     *
     * @param value
     * @return
     * @throws HibernateException
     */
    public Object deepCopy(Object value) throws HibernateException {
        // TODO 暂不实现
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        return x.equals(y);
    }
}
