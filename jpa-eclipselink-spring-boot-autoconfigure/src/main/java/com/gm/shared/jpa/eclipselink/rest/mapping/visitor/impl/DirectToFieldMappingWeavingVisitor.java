package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.springframework.stereotype.Component;

@Component
public class DirectToFieldMappingWeavingVisitor implements MappingWeavingVisitor<DirectToFieldMapping> {


    @Override
    public void visit(MappingWeavingContext context) {

    }

    @Override
    public Class<DirectToFieldMapping> getDatabaseMapping() {
        return DirectToFieldMapping.class;
    }

}
