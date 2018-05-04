package com.gm.shared.jpa.eclipselink.rest.mapping.weaver;

import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingVisitorLocator;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Map;

@Configurable
// TODO add load time aspectJ weaving for @Configurable
public class MappingWeaver {

    private MappingVisitorLocator locator;
    private Map<Class<?>, MappingWeavingContext> metadata;

    public void weave(Session session) {

        session.getDescriptors().forEach((aClass, cd) -> {
            doWeaveXmlClassDescriptor(session, cd);

        });

    }

    private void doWeaveXmlClassDescriptor(Session session, ClassDescriptor cd) {

        Class<?> aClass = cd.getJavaClass();
        MappingWeavingContext weavingContext = metadata.get(aClass) != null ? metadata.get(aClass) : new MappingWeavingContext(cd);




        cd.getMappings().forEach(mapping -> {

            MappingWeavingVisitor<DatabaseMapping> visitor = locator.visitorForMapping(mapping);
            visitor.visit(weavingContext);
        });

        while (weavingContext.hasReferencedClass()) {
            Class<?> referencedClass = weavingContext.getReferencedClass();
            // recurse, till there are no mappings
            doWeaveXmlClassDescriptor(session, session.getClassDescriptor(aClass));
        }
    }
}
