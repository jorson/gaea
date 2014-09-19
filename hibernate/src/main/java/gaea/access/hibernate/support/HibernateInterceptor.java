package gaea.access.hibernate.support;

import gaea.foundation.core.bo.AuditableEntityObject;
import gaea.foundation.core.context.WorkContext;
import gaea.foundation.core.repository.shard.PartitionInfo;
import gaea.foundation.core.repository.shard.ShardDataRegister;
import gaea.foundation.core.repository.shard.ShardParameter;
import gaea.foundation.core.repository.support.UserInfoAware;
import gaea.foundation.core.utils.CollectionUtils;
import gaea.foundation.core.utils.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 在对可审查的对象进行变更时，对相应的栏位进行变更。
 * <p/>
 *
 * @author wuhy
 */
public class HibernateInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;

    private UserInfoAware userInfoAware;

    /**
     * 更新对象
     *
     * @param entity
     * @param id
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @param types
     * @return
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        if (isAuditable(entity)) {
            processAuditableEntityObject(entity, true, propertyNames, currentState);
            return true;
        }
        return false;
    }

    /**
     * 新增对象
     *
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     * @return
     */
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (isAuditable(entity)) {
            processAuditableEntityObject(entity, false, propertyNames, state);
            return true;
        }
        return false;
    }

    /**
     * 是否为可审查的对象
     * <p/>
     * 如果是则返回为TRUE，否则返回为FALSE
     *
     * @param proxy 对象
     * @return
     */
    public boolean isAuditable(Object proxy) {
        return proxy instanceof AuditableEntityObject;
    }

    public Object resolveProxy(Object proxy) {
        return HibernateUtils.resolveRealObject(proxy);
    }

    /**
     * 处理可审查对象，当为可审查对象时，则<code>insertUser</code>、<code>updateUser</code>则更新成当前登录用户
     * <p>当为更新时，<code>insertUser</code>不会被改变</p>
     * <code>insertDate</code>、<code>updateDate</code>使用数据库时间
     *
     * @param entity
     * @param isUpdate
     * @param propertyNames
     * @param currentState
     */
    public void processAuditableEntityObject(Object entity, boolean isUpdate, String[] propertyNames, Object[] currentState) {
        AuditableEntityObject entityObject = (AuditableEntityObject) resolveProxy(entity);
        if (isUpdate) {
            entityObject.setUpdateUser(getCurrentUserId());
            entityObject.setUpdateDate(getCurrentUserLocalDate());
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].equals("updateUser")) {
                    currentState[i] = getCurrentUserId();
                }
                if (propertyNames[i].equals("updateDate")) {
                    currentState[i] = getCurrentUserLocalDate();
                }
            }
        } else {
            entityObject.setInsertUser(getCurrentUserId());
            entityObject.setInsertDate(getCurrentUserLocalDate());
            entityObject.setUpdateUser(getCurrentUserId());
            entityObject.setUpdateDate(getCurrentUserLocalDate());
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].equals("updateUser")) {
                    currentState[i] = getCurrentUserId();
                }
                if (propertyNames[i].equals("updateDate")) {
                    currentState[i] = getCurrentUserLocalDate();
                }
                if (propertyNames[i].equals("insertUser")) {
                    currentState[i] = getCurrentUserId();
                }
                if (propertyNames[i].equals("insertDate")) {
                    currentState[i] = getCurrentUserLocalDate();
                }
            }
        }
    }

    @Override
    public String onPrepareStatement(String sql) {
        List<PartitionInfo> datas = ShardDataRegister.getPartitionInfos();
        if (!CollectionUtils.isEmpty(datas)) {
            for (int i = 0; i < datas.size(); i++) {
                PartitionInfo partitionInfo = datas.get(i);
                if (!StringUtils.isEmpty(partitionInfo.getTableName()) && !StringUtils.isEmpty(partitionInfo.getReadTableName())) {
                    sql = sql.replace(partitionInfo.getTableName(), partitionInfo.getReadTableName());
                }
            }
        }
        //ShardDataRegister.clear();
        return sql;
    }


    /**
     * 取得当前登录人员的ID号
     *
     * @return
     */
    public String getCurrentUserId() {
        if (getUserInfoAware() != null) {
            return String.valueOf(getUserInfoAware().getCurrentUserId());
        }
        return null;
    }

    /**
     * 取得当前日期
     *
     * @return
     */
    public Date getCurrentUserLocalDate() {
        if (getUserInfoAware() != null) {
            return getUserInfoAware().getCurrentUserLocalDate();
        }
        return null;
    }

    public UserInfoAware getUserInfoAware() {
        return userInfoAware;
    }

    public void setUserInfoAware(UserInfoAware userInfoAware) {
        this.userInfoAware = userInfoAware;
    }
}
