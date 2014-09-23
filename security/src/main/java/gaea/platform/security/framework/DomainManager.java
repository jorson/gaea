package gaea.platform.security.framework;

import gaea.platform.security.domain.Domain;

import java.util.*;

/**
 * 域管理器
 * Created by wuhy on 14-6-6.
 */
public class DomainManager {

    // Map<Domain Key，Domain>
    private static final Map<String, Domain> DOMAIN_HASH_MAP = new HashMap<String, Domain>();

    /**
     * 获取所有域
     * @return
     */
    public static final List<Domain> getAll() {
        return new ArrayList(DOMAIN_HASH_MAP.values());
    }

    /**
     * 添加域
     * @param domain
     */
    public static final void add(Domain domain) {
        DOMAIN_HASH_MAP.put(domain.getId().toString(), domain);
    }

    /**
     *
     * @param domain
     * @return
     */
    public static final boolean contains(Domain domain) {
        return DOMAIN_HASH_MAP.containsKey(domain.getId());
    }
}
