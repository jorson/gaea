package com.nd.gaea.repository.hibernate.visitor;

import com.nd.gaea.repository.hibernate.mapping.model.*;
import com.nd.gaea.repository.hibernate.mapping.model.classbased.ClassMapping;
import com.nd.gaea.repository.hibernate.mapping.model.identity.GeneratorMapping;
import com.nd.gaea.repository.hibernate.mapping.model.identity.IdMapping;
import com.nd.gaea.repository.hibernate.mapping.model.identity.IdentityMapping;

import java.util.List;

/**
 * Created by Jorson on 2015/3/20.
 */
public interface MappingModelVisitor {

    void processId(IdMapping idMapping);
    void processClass(ClassMapping classMapping);
    void processColumn(ColumnMapping columnMapping);
    void processGenerator(GeneratorMapping generatorMapping);
    void processHibernateMapping(HibernateMapping hibernateMapping);
    void processProperty(PropertyMapping propertyMapping);
    void processNaturalId(NaturalIdMapping naturalIdMapping);

    void visit(List<HibernateMapping> mappings);

    void visit(IdMapping mapping);
    void visit(IdentityMapping mapping);
    void visit(ClassMapping classMapping);
    void visit(ColumnMapping columnMapping);
    void visit(GeneratorMapping generatorMapping);
    void visit(PropertyMapping propertyMapping);
    void visit(NaturalIdMapping naturalIdMapping);
}
