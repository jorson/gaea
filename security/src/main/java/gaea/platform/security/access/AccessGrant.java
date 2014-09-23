package gaea.platform.security.access;

import java.io.Serializable;

/**
 * 用户认证信息
 * @author wuhy
 * @date 14-8-26
 */
public interface AccessGrant extends Serializable {

    /**
     * 获取AccessToken
     * @return
     */
    public String getAccessToken();
}
