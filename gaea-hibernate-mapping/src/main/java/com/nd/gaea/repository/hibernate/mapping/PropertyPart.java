package com.nd.gaea.repository.hibernate.mapping;

import com.nd.gaea.repository.hibernate.mapping.model.*;
import com.nd.gaea.repository.hibernate.mapping.provider.PropertyMappingProvider;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping
 * @since 2015-04-14
 */
public class PropertyPart implements PropertyMappingProvider {

    private final Field field;
    private final AttributeStore attributes = new AttributeStore();
    private final AttributeStore columnAttributes = new AttributeStore();
    private final Class parentClazz;
    private final ColumnMappingCollection<PropertyPart> columns;
    private boolean nextBool = true;

    public PropertyPart(Field field, Class parentClazz) {
        this.field = field;
        this.parentClazz = parentClazz;
        this.columns = new ColumnMappingCollection<PropertyPart>(this);
    }

    @Override
    public PropertyMapping getPropertyMapping() {
        PropertyMapping mapping = new PropertyMapping(attributes.clone());
        mapping.setContainingEntityType(parentClazz);
        mapping.setField(field);

        if(columns.getCount() == 0 && mapping.isSpecified("Formula")) {
            ColumnMapping columnMapping = new ColumnMapping(this.columnAttributes.clone());
            columnMapping.set(ConstElementKey.ELEMENT_NAME, Layer.DEFAULTS, field.getName());
            mapping.addColumn(Layer.DEFAULTS, columnMapping);
        }

        Iterator<ColumnMapping> colIterator = this.columns.getIterator();
        while (colIterator.hasNext()) {
            ColumnMapping columnMapping = colIterator.next();
            mapping.addColumn(Layer.USER_SUPPLIED, columnMapping);
        }

        for (ColumnMapping column : mapping.getColumns()) {
            if(field.isEnumConstant()) {
                column.set("NotNull", Layer.DEFAULTS, false);
            }
            column.mergeAttribute(columnAttributes);
        }

        mapping.set(ConstElementKey.ELEMENT_NAME, Layer.DEFAULTS, mapping.getField().getName());
        mapping.set(ConstElementKey.ELEMENT_TYPE, Layer.DEFAULTS, getDefaultType());

        return mapping;
    }

    public PropertyPart setLength(int length) {
        columnAttributes.set("Length", Layer.USER_SUPPLIED, length);
        return this;
    }

    public PropertyPart setNullable() {
        columnAttributes.set("NotNull", Layer.USER_SUPPLIED, !nextBool);
        nextBool = true;
        return this;
    }

    public PropertyPart setReadOnly() {
        attributes.set("Insert", Layer.USER_SUPPLIED, !nextBool);
        attributes.set("Update", Layer.USER_SUPPLIED, !nextBool);
        nextBool = true;
        return this;
    }

    public PropertyPart setFormula(String formula) {
        attributes.set("Formula", Layer.USER_SUPPLIED, formula);
        return this;
    }

    public PropertyPart setLazyLoad() {
        attributes.set("Lazy", Layer.USER_SUPPLIED, nextBool);
        nextBool = true;
        return this;
    }

    public PropertyPart setIndex(String index) {
        columnAttributes.set("Index", Layer.USER_SUPPLIED, index);
        return this;
    }

    public PropertyPart column(String columnName) {
        this.columns.clear();
        this.columns.add(columnName);
        return this;
    }

    public PropertyPart setCustomType(String type) {
        attributes.set("Type", Layer.USER_SUPPLIED, new TypeReference(type));
        return this;
    }

    public PropertyPart setCustomSqlType(String sqlType) {
        columnAttributes.set("SqlType", Layer.USER_SUPPLIED, sqlType);
        return this;
    }

    public PropertyPart setUnique() {
        columnAttributes.set("Unique", Layer.USER_SUPPLIED, nextBool);
        nextBool = true;
        return this;
    }

    public PropertyPart setPrecision(int precision) {
        columnAttributes.set("Precision", Layer.USER_SUPPLIED, precision);
        return this;
    }

    public PropertyPart setScale(int scale) {
        columnAttributes.set("Scale", Layer.USER_SUPPLIED, scale);
        return this;
    }

    public PropertyPart setDefault(String value) {
        columnAttributes.set("Default", Layer.USER_SUPPLIED, value);
        return this;
    }

    public PropertyPart setUniqueKey(String keyName) {
        columnAttributes.set("UniqueKey", Layer.USER_SUPPLIED, keyName);
        return this;
    }

    public PropertyPart setCheck(String constraint) {
        columnAttributes.set("Check", Layer.USER_SUPPLIED, constraint);
        return this;
    }

    public PropertyPart setOptimisticLock() {
        attributes.set("OptimisticLock", Layer.USER_SUPPLIED, nextBool);
        nextBool = true;
        return this;
    }

    public PropertyPart isNot() {
        nextBool = !nextBool;
        return this;
    }

    private TypeReference getDefaultType() {
        TypeReference reference = new TypeReference(field.getType());
        return reference;
    }
}
