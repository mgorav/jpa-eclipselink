package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor.DirectToFieldMappingAttributeAccessor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.oxm.mappings.XMLDirectMapping;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class DirectToFieldMappingWeavingVisitor implements MappingWeavingVisitor<DirectToFieldMapping> {


    @Override
    public void visit(MappingWeavingContext<DirectToFieldMapping> context) {

        DirectToFieldMapping directToFieldMapping = context.getCurrentDatabaseMapping();
        String attributeName = directToFieldMapping.getAttributeName();

        if (context.mappingDoesNotExistFor(attributeName)) {

            final XMLDirectMapping xmlValueDirectMapping = new XMLDirectMapping();
            xmlValueDirectMapping.setIsCDATA(true);
            // TODO
            //xmlValueDirectMapping.setNullPolicy(new ConfigurableNullPolicy());


            xmlValueDirectMapping.setAttributeName(attributeName);

            xmlValueDirectMapping.setAttributeAccessor(new DirectToFieldMappingAttributeAccessor(directToFieldMapping));
            xmlValueDirectMapping.setAttributeClassification(directToFieldMapping.getAttributeClassification());

            xmlValueDirectMapping.setXPath(format("%s%s%s", '@', attributeName, "/text()"));

            context.addMapping(xmlValueDirectMapping);
        }


    }

    @Override
    public Class<DirectToFieldMapping> getDatabaseMapping() {
        return DirectToFieldMapping.class;
    }

}
