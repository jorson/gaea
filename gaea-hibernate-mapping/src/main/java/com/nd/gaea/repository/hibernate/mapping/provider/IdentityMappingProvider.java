package com.nd.gaea.repository.hibernate.mapping.provider;

import com.nd.gaea.repository.hibernate.mapping.model.identity.IdMapping;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.provider
 * @since 2015-03-30
 */
public interface IdentityMappingProvider {

    IdMapping getIdentityMapping();
}
