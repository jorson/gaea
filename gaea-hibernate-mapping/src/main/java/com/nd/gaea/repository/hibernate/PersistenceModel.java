package com.nd.gaea.repository.hibernate;

import com.nd.gaea.repository.hibernate.mapping.model.HibernateMapping;
import com.nd.gaea.repository.hibernate.mapping.model.writer.MappingXmlSerializer;
import com.nd.gaea.repository.hibernate.mapping.provider.MappingProvider;
import com.nd.gaea.repository.hibernate.visitor.MappingModelVisitor;
import com.nd.gaea.repository.hibernate.visitor.ValidationVisitor;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.hibernate.cfg.Configuration;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo
 * @since 2015-03-26
 */
public class PersistenceModel {

    protected final List<MappingProvider> classProviders = new ArrayList<MappingProvider>();
    private final List<MappingModelVisitor> visitors = new ArrayList<MappingModelVisitor>();
    private List<HibernateMapping> compiledMappings;
    private ValidationVisitor validationVisitor;

    public PersistenceModel() {
/*        visitors.add(new SeparateSubclassVisitor());
        visitors.add(validationVisitor = new ValidationVisitor());*/
    }

    public void configure(Configuration cfg) {

        ensureMappingsBuilt();

        for(HibernateMapping mapping : compiledMappings) {
            if(mapping.getClassMappings().size() > 0) {
                MappingXmlSerializer serializer = new MappingXmlSerializer();
                Document document = serializer.serialize(mapping);
                System.out.println(document.asXML());
                cfg.addDocument(convertDom4jToW3c(document));
            } else {
                MappingXmlSerializer serializer = new MappingXmlSerializer();
                Document document = serializer.serialize(mapping);

                if(cfg.getClassMapping(mapping.getClassMappings().get(0).getEntityName()) == null) {
                    cfg.addDocument(convertDom4jToW3c(document));
                }
            }
        }
    }

    public void add(Class clazz) {
        try {
            Object mapping = instantiateUsingParameterlessConstructor(clazz);

            if(mapping instanceof MappingProvider) {
                add((MappingProvider)mapping);
            } else {
                throw new UnsupportedOperationException("unsupported mapping type '" +
                clazz.getName() + "'");
            }

        } catch (MissingConstructorException e) {
            e.printStackTrace();
        }
    }

    public void add(MappingProvider provider) {
        classProviders.add(provider);
    }

    public List<HibernateMapping> buildMappings() {
        final List<HibernateMapping> hibernateMappings = new ArrayList<HibernateMapping>();
        buildSeparateMappings(new AddHibernateMapping() {
            @Override
            public void addMapping(HibernateMapping hibernateMapping) {
                hibernateMappings.add(hibernateMapping);
            }
        });

        applyVisitors(hibernateMappings);

        return hibernateMappings;
    }

    private void buildSeparateMappings(AddHibernateMapping addHibernateMapping) {
        for(MappingProvider classMap : classProviders) {
            HibernateMapping hbm = classMap.getHibernateMapping();
            hbm.addClass(classMap.getClassMapping());
            addHibernateMapping.addMapping(hbm);
        }
    }

    private void ensureMappingsBuilt() {
        if(compiledMappings != null) {
            return;
        }
        compiledMappings = buildMappings();
    }

    private void applyVisitors(List<HibernateMapping> mappings) {
        for(MappingModelVisitor visitor : visitors) {
            visitor.visit(mappings);
        }
    }

    private Object instantiateUsingParameterlessConstructor(Class clazz) throws MissingConstructorException {
        try {
            Constructor constructor = clazz.getConstructor(null);
            return constructor.newInstance(null);
        } catch (Exception ex) {
            throw new MissingConstructorException(clazz);
        }
    }

    private org.w3c.dom.Document convertDom4jToW3c(Document originalDocument) {
        String xmlString = originalDocument.asXML();
        org.w3c.dom.Document resultDocument = null;
        try {
            if(StringUtils.isNotEmpty(xmlString)) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                resultDocument = builder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))));
            } else {
                throw new NullPointerException("originalDocument is NULL");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultDocument;
    }

    public interface AddHibernateMapping {
        public void addMapping(HibernateMapping hibernateMapping);
    }
}
