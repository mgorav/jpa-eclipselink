package com.gm.shared.jpa.eclipselink.rest.mapping.weaver;

import com.gm.shared.jpa.eclipselink.customizer.JpaEclipseLinkCustomizer;
import com.gm.shared.jpa.eclipselink.rest.mapping.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.mapping.visitor.locator.MappingVisitorLocator;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.valueOf;

@Component
public class MappingWeavingCustomizer<M extends DatabaseMapping, V extends MappingWeavingVisitor<M>> implements JpaEclipseLinkCustomizer {

    @Autowired
    private MappingVisitorLocator<M, V> locator;
    private Map<Class<?>, MappingWeavingContext> metadata;


    @Override
    public void customize(Session session) throws Exception {
        this.metadata = new HashMap<>(session.getDescriptors().size());
        session.getDescriptors().forEach((aClass, cd) -> {
            Project project = new Project();
            doWeaveXmlClassDescriptor(session, project, cd);

        });
    }

    @Override
    public int getOrder() {
        return MAX_VALUE - 2;
    }

    @Override
    public int compareTo(JpaEclipseLinkCustomizer o) {
        return valueOf(o.getOrder()).compareTo(valueOf(this.getOrder()));
    }


    private void doWeaveXmlClassDescriptor(Session session, Project project, ClassDescriptor cd) {

        Class<?> aClass = cd.getJavaClass();
        MappingWeavingContext weavingContext = metadata.get(aClass) != null ? metadata.get(aClass) : new MappingWeavingContext(project, cd);


        cd.getMappings().forEach(mapping -> {

            weavingContext.setCurrentDatabaseMapping(mapping);
            MappingWeavingVisitor<DatabaseMapping> visitor = locator.visitorForMapping(mapping);
            visitor.visit(weavingContext);
        });

        while (weavingContext.hasReferencedClass()) {
            Class<?> referencedClass = weavingContext.getReferencedClass();
            // recurse, till there are no mappings
            doWeaveXmlClassDescriptor(session, project, session.getClassDescriptor(aClass));
        }
    }

}
