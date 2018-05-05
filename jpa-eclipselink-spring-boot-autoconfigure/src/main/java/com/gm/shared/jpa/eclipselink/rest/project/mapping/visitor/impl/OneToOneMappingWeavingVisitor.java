package com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;

@Component
public class OneToOneMappingWeavingVisitor implements MappingWeavingVisitor<OneToOneMapping> {

    @Autowired
    private ForeignReferenceMappingVisitor fkVisitor;

    @Override
    public void visit(MappingWeavingContext<OneToOneMapping> context) {
        // Handles same as ManyToOne
        MappingWeavingContext<ForeignReferenceMapping> fkContext = uncheckedCast(context);
        fkVisitor.visit(fkContext);
    }

    @Override
    public Class<OneToOneMapping> getDatabaseMapping() {
        return OneToOneMapping.class;
    }
}
