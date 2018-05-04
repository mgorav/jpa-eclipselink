package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.springframework.stereotype.Component;

@Component
public class ManyToOneMappingWeavingVisitor implements MappingWeavingVisitor<ManyToOneMapping> {

    @Override
    public void visit(MappingWeavingContext context) {

    }

    @Override
    public Class<ManyToOneMapping> getDatabaseMapping() {
        return ManyToOneMapping.class;
    }
}
