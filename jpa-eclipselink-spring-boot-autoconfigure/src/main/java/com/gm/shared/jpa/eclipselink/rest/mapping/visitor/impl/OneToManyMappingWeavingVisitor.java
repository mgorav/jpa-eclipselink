package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.springframework.stereotype.Component;

@Component
public class OneToManyMappingWeavingVisitor implements MappingWeavingVisitor<OneToManyMapping> {

    @Override
    public void visit(ClassDescriptor classDescriptor, XMLDescriptor xmlDescriptor, OneToManyMapping databaseMapping) {

    }
}
