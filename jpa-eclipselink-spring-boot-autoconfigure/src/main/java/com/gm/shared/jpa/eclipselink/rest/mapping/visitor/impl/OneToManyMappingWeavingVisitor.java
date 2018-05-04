package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.springframework.stereotype.Component;

@Component
public class OneToManyMappingWeavingVisitor implements MappingWeavingVisitor<OneToManyMapping> {

    @Override
    public void visit(MappingWeavingContext context) {

    }

    @Override
    public Class<OneToManyMapping> getDatabaseMapping() {
        return OneToManyMapping.class;
    }
}
