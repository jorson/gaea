package gaea.platform.security.framework;

import gaea.foundation.core.utils.CollectionUtils;
import gaea.platform.security.access.User;
import gaea.platform.security.context.UserContext;

import java.util.List;

/**
 * Created by wuhy on 14-7-14.
 */
public class PurviewJudger {

    /**
     * 判断用户是否有权限
     * @param purviewKey    权限
     * @param user          当前用户
     * @return
     */
    public static final boolean hasPurview(String purviewKey, User user) {
        List<String> purviewKeys = user.getPurviewKeys();
        if (CollectionUtils.isEmpty(purviewKeys)) {
            return false;
        }
        return purviewKeys.contains(purviewKey);
    }

    /**
     * 判断用户是否有权限
     * @param purviewKey    权限
     * @return
     */
    public static final boolean hasPurview(String purviewKey) {
        User user = UserContext.getCurrentUser();
        List<String> purviewKeys = user.getPurviewKeys();
        if (CollectionUtils.isEmpty(purviewKeys)) {
            return false;
        }
        return purviewKeys.contains(purviewKey);
    }
}
