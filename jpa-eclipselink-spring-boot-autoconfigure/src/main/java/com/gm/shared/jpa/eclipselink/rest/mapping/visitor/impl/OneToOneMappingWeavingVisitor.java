package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.springframework.beans.factory.annotation.Autowired;

public class OneToOneMappingWeavingVisitor implements MappingWeavingVisitor<OneToOneMapping> {

    @Autowired
    private ManyToOneMappingWeavingVisitor manyToOneMappingWeavingVisitor;

    @Override
    public void visit(MappingWeavingContext context) {
        // Handles same as ManyToOne
        manyToOneMappingWeavingVisitor.visit(context);
    }

    @Override
    public Class<OneToOneMapping> getDatabaseMapping() {
        return OneToOneMapping.class;
    }
}
