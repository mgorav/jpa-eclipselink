package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.weaving.customizer;

import com.gm.shared.jpa.eclipselink.customizer.JpaEclipseLinkCustomizer;
import com.gm.shared.jpa.eclipselink.rest.Wrapper;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.hateoas.LinkMapping;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.visitor.locator.MappingVisitorLocator;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.weaving.context.MappingWeavingContext;
import com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.project.ProjectService;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLAnyCollectionMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.valueOf;

@Component
public class MappingWeavingCustomizer<M extends DatabaseMapping, V extends MappingWeavingVisitor<M>> implements JpaEclipseLinkCustomizer {

    @Autowired
    private MappingVisitorLocator<M, V> locator;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private LinkMapping linkMapping;


    @Override
    public void customize(Session session) throws Exception {

        session.getDescriptors().forEach((aClass, cd) -> {
            Project project = projectService.newProject();
            List<Class<?>> processedClasses = new ArrayList<>(100);
            Map<Class<?>, MappingWeavingContext> metadata = new HashMap<>(session.getDescriptors().size());
            doWeaveXmlClassDescriptor(processedClasses, session, project, cd, metadata);

            addWrapperSupport(metadata.get(cd.getJavaClass()));
            projectService.createXmlContext(session, project, aClass);


        });
    }


    private void doWeaveXmlClassDescriptor(List<Class<?>> processedClasses, Session session, Project project,
                                           ClassDescriptor cd, Map<Class<?>, MappingWeavingContext> metadata) {

        Class<?> aClass = cd.getJavaClass();

        if (processedClasses.contains(aClass)) {
            // break recurssion, if XMLDescriptor is already present
            return;
        }

        MappingWeavingContext weavingContext = metadata.get(aClass) != null ? metadata.get(aClass) : new MappingWeavingContext(session, project, cd);
        metadata.putIfAbsent(aClass, weavingContext);

        // REST Link if not there

        if (weavingContext.getCurrentXMLDescriptor(aClass).getMappingForAttributeName("link") == null) {
            linkMapping.constructRestLink(weavingContext);
        }

        cd.getMappings().forEach(mapping -> {

            weavingContext.setCurrentDatabaseMapping(mapping);
            MappingWeavingVisitor<M> visitor = locator.visitorForMapping(uncheckedCast(mapping));
            visitor.visit(weavingContext);
        });

        processedClasses.add(cd.getJavaClass());

        while (weavingContext.hasReferencedClass()) {
            Class<?> referencedClass = weavingContext.getReferencedClass();
            // recurse, till there are no mappings
            doWeaveXmlClassDescriptor(processedClasses, session, project, session.getClassDescriptor(referencedClass), metadata);
        }
    }


    private void addWrapperSupport(MappingWeavingContext context) {

        XMLDescriptor wrapperDescriptor = context.newXMLDescriptor(Wrapper.class);
        wrapperDescriptor.setDefaultRootElement("items");
        // XmlAnyCollectionMapping to handle List<T>
        XMLAnyCollectionMapping mapping = new XMLAnyCollectionMapping();
        mapping.setAttributeName(Wrapper.class.getName());
        mapping.setGetMethodName("getItems");
        mapping.setSetMethodName("setItems");
        wrapperDescriptor.addMapping(mapping);
    }

    @Override
    public int getOrder() {
        return MAX_VALUE - 2;
    }

    @Override
    public int compareTo(JpaEclipseLinkCustomizer o) {
        return valueOf(o.getOrder()).compareTo(valueOf(this.getOrder()));
    }

}
