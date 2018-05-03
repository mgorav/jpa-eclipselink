package com.gm.shared.jpa.eclipselink.rest.mapping.visitor;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;

public interface MappingWeavingVisitor<T extends DatabaseMapping> {

    /**
     * Visit JPA mapping {@link DatabaseMapping} of {@link ClassDescriptor} & covert it
     * {@link org.eclipse.persistence.oxm.mappings.XMLMapping} & add it to {@link XMLDescriptor}
     *
     * @param classDescriptor
     * @param xmlDescriptor
     * @param databaseMapping
     */
    void visit(ClassDescriptor classDescriptor, XMLDescriptor xmlDescriptor, T databaseMapping);

}
