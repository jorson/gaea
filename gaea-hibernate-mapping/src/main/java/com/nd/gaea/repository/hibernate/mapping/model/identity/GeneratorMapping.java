package com.nd.gaea.repository.hibernate.mapping.model.identity;

import com.nd.gaea.repository.hibernate.mapping.MappingBase;
import com.nd.gaea.repository.hibernate.mapping.model.AttributeStore;
import com.nd.gaea.repository.hibernate.visitor.MappingModelVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.model.identity
 * @since 2015-03-24
 */
public class GeneratorMapping extends MappingBase {

    private final AttributeStore attributes = new AttributeStore();
    private Map<String, String> params;
    private Class containingEntityType;

    public GeneratorMapping() {
        params = new HashMap<String, String>();
    }

    @Override
    public void acceptVisitor(MappingModelVisitor visitor) {
        visitor.processGenerator(this);
    }

    @Override
    public boolean isSpecified(String attribute) {
        return attributes.isSpecified(attribute);
    }

    @Override
    public void set(String attribute, int layer, Object value) {
        this.attributes.set(attribute, layer, value);
    }

    public String getClassName() {
        return attributes.getOrDefault("Class");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Class getContainingEntityType() {
        return containingEntityType;
    }

    public void setContainingEntityType(Class containingEntityType) {
        this.containingEntityType = containingEntityType;
    }
}
