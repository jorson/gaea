package gaea.platform.security.utils;

import gaea.foundation.core.config.ConfigConstant;
import gaea.foundation.core.config.SystemConfig;
import org.springframework.util.Assert;

/**
 * 域实例KEY的帮助器
 * User: wuhy
 * Date: 14-5-30
 * Time: 下午4:27
 */
public abstract class DomainInstanceKeyHelper {

    /**
     * 根据DomainInstanceKey解析DomainKey
     *
     * @param instanceKey DomainInstanceKey 改Key为各业务系统生成
     * @return
     */
    public static String getDomainKeyFromInstanceKey(String instanceKey) {
        Assert.hasLength(instanceKey);

        // 兼容C#
        if (instanceKey.startsWith("$") && instanceKey.endsWith("$")) {
            return instanceKey.substring(1, instanceKey.length() - 2);
        }

        int index = instanceKey.indexOf(':');
        if (index != -1) {
            throw new IllegalArgumentException("无法获取到 gaea.platform.security.domain id，instanceKey 必须为如下格式 {domainKey}:{...}。");
        }
        return instanceKey.substring(0, index);
    }

    /**
     * 获取DomainInstanceKey
     *
     * @return DomainInstanceKey
     */
    public static String getInstanceKey(String domainKey) {
        //instance key = <security.gaea.platform.security.domain>:<prject>
        String projectName = SystemConfig.Instance.getProperty(ConfigConstant.PROJECT_NAME);
        return domainKey + ":" + projectName;
    }

}
