package gaea.foundation.core.repository.support;

import java.util.Date;

/**
 * 用户信息的接口，用于在Core取得用户的信息
 *
 * @author wuhy
 */
public interface UserInfoAware {
    /**
     * 取得当前用户的ID
     *
     * @return
     */
    Object getCurrentUserId();

    /**
     * 取得当前用户的本地日期
     *
     * @return
     */
    Date getCurrentUserLocalDate();
}
