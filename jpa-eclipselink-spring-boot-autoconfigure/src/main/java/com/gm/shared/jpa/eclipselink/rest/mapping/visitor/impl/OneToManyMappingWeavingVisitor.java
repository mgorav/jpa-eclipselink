package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor.OneToManyMappingAttributeAccessor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.internal.queries.ContainerPolicy;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.oxm.mappings.XMLCompositeCollectionMapping;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class OneToManyMappingWeavingVisitor implements MappingWeavingVisitor<OneToManyMapping> {

    @Override
    public void visit(MappingWeavingContext<OneToManyMapping> context) {
        OneToManyMapping oneToManyMapping = context.getCurrentDatabaseMapping();
        Class<?> refClass = oneToManyMapping.getReferenceClass();
        String attributeName = oneToManyMapping.getAttributeName();

        if (context.mappingDoesNotExistFor(attributeName)) {
            final ContainerPolicy containerPolicy = oneToManyMapping.getContainerPolicy();

            final XMLCompositeCollectionMapping compositeCollectionMapping = new XMLCompositeCollectionMapping();
            compositeCollectionMapping.setDefaultEmptyContainer(false);
            String postFix = oneToManyMapping.getReferenceClass().getSimpleName();
            compositeCollectionMapping.setAttributeName(attributeName);

            compositeCollectionMapping.setAttributeAccessor(new OneToManyMappingAttributeAccessor(oneToManyMapping));

            compositeCollectionMapping.setReferenceClass(refClass);

            compositeCollectionMapping.setContainerPolicy(containerPolicy);

            compositeCollectionMapping.setXPath(format("%s%s%s", attributeName, '/', postFix));

            context.addMapping(compositeCollectionMapping);


        }
    }

    @Override
    public Class<OneToManyMapping> getDatabaseMapping() {
        return OneToManyMapping.class;
    }
}
