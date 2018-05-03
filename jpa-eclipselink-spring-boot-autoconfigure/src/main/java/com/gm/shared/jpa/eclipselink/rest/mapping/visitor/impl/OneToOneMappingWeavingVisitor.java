package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.springframework.stereotype.Component;

@Component
public class OneToOneMappingWeavingVisitor implements MappingWeavingVisitor<OneToOneMapping> {

    @Override
    public void visit(ClassDescriptor classDescriptor, XMLDescriptor xmlDescriptor, OneToOneMapping databaseMapping) {

    }
}
