package com.nd.gaea.repository.hibernate.mapping.model.writer;

import com.nd.gaea.repository.hibernate.mapping.model.identity.GeneratorMapping;
import com.nd.gaea.repository.hibernate.visitor.NullMappingModelVisitor;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Map;

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
public class XmlGeneratorWriter extends NullMappingModelVisitor implements XmlWriter<GeneratorMapping> {

    private Document document;

    @Override
    public Document write(GeneratorMapping mappingModel) {
        document = null;
        mappingModel.acceptVisitor(this);
        return document;
    }

    @Override
    public void processGenerator(GeneratorMapping generatorMapping) {
        document = DocumentHelper.createDocument();
        Element element = document.addElement("generator");

        if(generatorMapping.isSpecified("Class")) {
            withAttr(element, "class", generatorMapping.getClassName());
        }

        for(Map.Entry<String, String> param : generatorMapping.getParams().entrySet()) {
            element.addElement("param")
                    .addAttribute("name", param.getKey())
                    .addText(param.getValue());
        }
    }
}
