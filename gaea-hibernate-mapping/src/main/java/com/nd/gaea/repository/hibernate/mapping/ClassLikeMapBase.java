package com.nd.gaea.repository.hibernate.mapping;

import com.nd.gaea.repository.hibernate.GenericUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author jorson.WHY
 * @package com.nd.demo.mapping
 * @since 2015-03-30
 */
public abstract class ClassLikeMapBase<T> {

    private final MappingProviderStore providers;

    protected ClassLikeMapBase(MappingProviderStore providers) {
        this.providers = providers;
    }

    public PropertyPart map(Field member) {
        return map(member, null);
    }

    public PropertyPart map(Field member, String columnName) {
        PropertyPart part = new PropertyPart(member, getClazz());
        if(StringUtils.isNotEmpty(columnName)) {
            part.column(columnName);
        }
        providers.getProperties().add(part);
        return part;
    }


    protected Class getClazz() {
        return GenericUtil.getFirstGenericParamClass(getClass());
    }
}
