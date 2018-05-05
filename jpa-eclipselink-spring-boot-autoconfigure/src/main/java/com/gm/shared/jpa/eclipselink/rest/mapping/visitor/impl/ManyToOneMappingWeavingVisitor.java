package com.gm.shared.jpa.eclipselink.rest.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;

@Component
public class ManyToOneMappingWeavingVisitor implements MappingWeavingVisitor<ManyToOneMapping> {

    @Autowired
    private ForeignReferenceMappingVisitor fkVisitor;

    @Override
    public void visit(MappingWeavingContext<ManyToOneMapping> context) {

        MappingWeavingContext<ForeignReferenceMapping> fkContext = uncheckedCast(context);

        fkVisitor.visit(fkContext);

    }


    @Override
    public Class<ManyToOneMapping> getDatabaseMapping() {
        return ManyToOneMapping.class;
    }

}
