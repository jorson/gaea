package gaea.foundation.core.repository.extend;

import gaea.foundation.core.bo.EntityObject;
import gaea.foundation.core.repository.extend.support.EntityInfo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: billy
 * Date: 12-3-29
 * Time: 下午10:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class UndeletableUtils {

    public static boolean isUnValid(EntityObject entity) {
        if (entity != null) {
            Class entityClass = entity.getClass();
            EntityInfo entityInfo = new EntityInfo(entityClass);
            if (entityInfo.isUndeletable()) {
                try {
                    Object value = PropertyUtils.getProperty(entity, entityInfo.getStatusProperty());
                    if (value.equals(entityInfo.getUnvalidValue())) {
                        return true;
                    }
                } catch (Exception e) {
                    ReflectionUtils.handleReflectionException(e);
                }
            }
        }
        return false;
    }
}