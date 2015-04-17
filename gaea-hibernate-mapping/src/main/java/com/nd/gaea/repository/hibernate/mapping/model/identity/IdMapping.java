package com.nd.gaea.repository.hibernate.mapping.model.identity;

import com.nd.gaea.repository.hibernate.mapping.model.AttributeStore;
import com.nd.gaea.repository.hibernate.mapping.model.ColumnBasedMappingBase;
import com.nd.gaea.repository.hibernate.mapping.model.ColumnMapping;
import com.nd.gaea.repository.hibernate.mapping.model.TypeReference;
import com.nd.gaea.repository.hibernate.visitor.MappingModelVisitor;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.model
 * @since 2015-03-23
 */
public class IdMapping extends ColumnBasedMappingBase implements IdentityMapping {

    private Class containingEntityType;

    public IdMapping() {
        this(new AttributeStore());
    }

    public IdMapping(AttributeStore underlyingStore) {
        super(underlyingStore);
    }

    @Override
    public void acceptVisitor(MappingModelVisitor visitor) {
        visitor.processId(this);

        for(ColumnMapping mapping : getColumns()) {
            visitor.visit(mapping);
        }

        if(this.getGenerator() != null) {
            visitor.visit(this.getGenerator());
        }
    }

    @Override
    public boolean isSpecified(String attribute) {
        return this.attributes.isSpecified(attribute);
    }

    @Override
    public void set(String attribute, int layer, Object value) {
        this.attributes.set(attribute, layer, value);
    }

    public String getAccess() {
        return this.attributes.getOrDefault("Access");
    }

    public String getName() {
        return this.attributes.getOrDefault("Name");
    }

    public TypeReference getType() {
        return this.attributes.getOrDefault("Type");
    }

    public String getUnsavedValue() {
        return this.attributes.getOrDefault("UnsavedValue");
    }

    public Class getContainingEntityType() {
        return containingEntityType;
    }

    public void setContainingEntityType(Class containingEntityType) {
        this.containingEntityType = containingEntityType;
    }

    public GeneratorMapping getGenerator() {
        return this.attributes.getOrDefault("Generator");
    }
}
