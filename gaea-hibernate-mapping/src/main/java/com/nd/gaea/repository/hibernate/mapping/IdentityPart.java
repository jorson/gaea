package com.nd.gaea.repository.hibernate.mapping;

import com.nd.gaea.repository.hibernate.mapping.model.AttributeStore;
import com.nd.gaea.repository.hibernate.mapping.model.ColumnMapping;
import com.nd.gaea.repository.hibernate.mapping.model.Layer;
import com.nd.gaea.repository.hibernate.mapping.model.TypeReference;
import com.nd.gaea.repository.hibernate.mapping.model.identity.GeneratorMapping;
import com.nd.gaea.repository.hibernate.mapping.model.identity.IdMapping;
import com.nd.gaea.repository.hibernate.mapping.provider.IdentityMappingProvider;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping
 * @since 2015-03-30
 */
public class IdentityPart implements IdentityMappingProvider {

    private final AttributeStore columnAttributes = new AttributeStore();
    private final AttributeStore attributes = new AttributeStore();
    private final List<String> columns = new ArrayList<String>();
    private IdentityGenerationStrategyBuilder<IdentityPart> generatedBy;

    private final Class entityClazz;
    private Class identityType;
    private boolean nextBool = true;
    private String name;
    private Field field;


    public IdentityPart(Class entity, Field field) {
        this.entityClazz = entity;
        this.field = field;
        this.identityType = field.getType();
        this.generatedBy = new IdentityGenerationStrategyBuilder<IdentityPart>(this, identityType, entityClazz);

        setName(field.getName());
        setDefaultGenerator();
    }

    @Override
    public IdMapping getIdentityMapping() {
        IdMapping mapping = new IdMapping(attributes.clone());
        mapping.setContainingEntityType(entityClazz);

        if(columns.size() > 0) {
            for(String column : columns) {
                ColumnMapping columnMapping = new ColumnMapping(columnAttributes.clone());
                columnMapping.set(ConstElementKey.ELEMENT_NAME, Layer.DEFAULTS, column);
                mapping.addColumn(Layer.USER_SUPPLIED, columnMapping);
            }
        } else if(hasNameSpecified()) {
            ColumnMapping columnMapping = new ColumnMapping(columnAttributes.clone());
            columnMapping.set(ConstElementKey.ELEMENT_NAME, Layer.DEFAULTS, name);
            mapping.addColumn(Layer.DEFAULTS, columnMapping);
        }

        if(this.field != null) {
            mapping.set(ConstElementKey.ELEMENT_NAME, Layer.DEFAULTS, name);
        }
        mapping.set(ConstElementKey.ELEMENT_TYPE, Layer.DEFAULTS, new TypeReference(identityType));

        if(this.getGeneratedBy().isDirty()) {
            mapping.set(ConstElementKey.ELEMENT_GENERATOR, Layer.USER_SUPPLIED, this.generatedBy.getGeneratorMapping());
        }

        return mapping;
    }

    /**
     * 设置默认的主键的创建者
     */
    private void setDefaultGenerator() {
        GeneratorMapping generatorMapping = new GeneratorMapping();
        GeneratorBuilder defaultGenerator = new GeneratorBuilder(generatorMapping, identityType,
                Layer.USER_SUPPLIED);

        //如果主键是UUID
        if(identityType.equals(UUID.class)) {
            defaultGenerator.uuidString();
        } else if(identityType.equals(Integer.class) || identityType.equals(Long.class)) {
            defaultGenerator.identity();
        } else {
            defaultGenerator.assigned();
        }

        attributes.set("Generator", Layer.DEFAULTS, generatorMapping);
    }

    public IdentityPart column(String columnName) {
        columns.clear();
        columns.add(columnName);
        return this;
    }

    public IdentityGenerationStrategyBuilder<IdentityPart> getGeneratedBy() {
        return this.generatedBy;
    }

    void setName(String newName) {
        this.name = newName;
    }

    boolean hasNameSpecified() {
        return !StringUtils.isEmpty(this.name);
    }
}
