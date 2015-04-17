package com.nd.gaea.repository.hibernate.mapping.model.writer;

import com.nd.gaea.repository.hibernate.mapping.model.ColumnMapping;
import com.nd.gaea.repository.hibernate.visitor.NullMappingModelVisitor;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import static com.nd.gaea.repository.hibernate.mapping.model.writer.XmlExtensions.*;
import static com.nd.gaea.repository.hibernate.mapping.model.writer.XmlExtensions.withAttr;

/**
 * 在这里输入标题
 * <p/>
 * 说明
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.model.writer
 * @since 2015-03-25
 */
public class XmlColumnWriter extends NullMappingModelVisitor implements XmlWriter<ColumnMapping> {

    private Document document;

    @Override
    public Document write(ColumnMapping mappingModel) {
        this.document = null;
        mappingModel.acceptVisitor(this);
        return document;
    }

    @Override
    public void processColumn(ColumnMapping columnMapping) {
        document = DocumentHelper.createDocument();
        Element element = document.addElement("column");

        if(columnMapping.isSpecified("Name")) {
            withAttr(element, "name", columnMapping.getName());
        }

        if (columnMapping.isSpecified("Check"))
            withAttr(element, "check", columnMapping.getCheck());

        if (columnMapping.isSpecified("Length"))
            withAttr(element, "length", columnMapping.getLength());

        if (columnMapping.isSpecified("Index"))
            withAttr(element, "index", columnMapping.getIndex());

        if (columnMapping.isSpecified("NotNull"))
            withAttr(element, "not-null", columnMapping.getNotNull());

        if (columnMapping.isSpecified("SqlType"))
            withAttr(element, "sql-type", columnMapping.getSqlType());

        if (columnMapping.isSpecified("Unique"))
            withAttr(element, "unique", columnMapping.getUnique());

        if (columnMapping.isSpecified("UniqueKey"))
            withAttr(element, "unique-key", columnMapping.getUniqueKey());

        if (columnMapping.isSpecified("Precision"))
            withAttr(element, "precision", columnMapping.getPrecision());

        if (columnMapping.isSpecified("Scale"))
            withAttr(element, "scale", columnMapping.getScale());

        if (columnMapping.isSpecified("Default"))
            withAttr(element, "default", columnMapping.getDefault());
    }
}
