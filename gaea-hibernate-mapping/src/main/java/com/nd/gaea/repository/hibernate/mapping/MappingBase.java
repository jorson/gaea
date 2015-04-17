package com.nd.gaea.repository.hibernate.mapping;

import com.nd.gaea.repository.hibernate.visitor.MappingModelVisitor;

/**
 * @author jorson.WHY
 * @package com.nd.demo.mapping
 * @since 2015-03-23
 */
public abstract class MappingBase implements Mapping {

    public abstract void acceptVisitor(MappingModelVisitor visitor);
    public abstract boolean isSpecified(String attribute);
    public abstract void set(String attribute, int layer, Object value);
}
