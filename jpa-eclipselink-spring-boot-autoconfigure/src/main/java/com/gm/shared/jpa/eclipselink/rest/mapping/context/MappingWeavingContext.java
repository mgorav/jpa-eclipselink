package com.gm.shared.jpa.eclipselink.rest.mapping.context;

import lombok.Data;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.sessions.Project;

import java.util.ArrayDeque;
import java.util.Queue;


@Data
public class MappingWeavingContext<T extends DatabaseMapping> {
    private final Project project;
    private final ClassDescriptor currentClassDescriptor;
    private T currentDatabaseMapping;
    private final Queue<Class<?>> referencedClasses;

    public MappingWeavingContext(ClassDescriptor currentClassDescriptor) {
        this.currentClassDescriptor = currentClassDescriptor;
        this.referencedClasses = new ArrayDeque<>();
        this.project = new Project();
    }

    public XMLDescriptor getXMLDescriptorFor(Class<?> aClass) {
        XMLDescriptor xmlDescriptor = (XMLDescriptor) project.getDescriptor(aClass);

        if (xmlDescriptor == null) {
            xmlDescriptor = new XMLDescriptor();
            project.addDescriptor(xmlDescriptor);
        }

        return xmlDescriptor;
    }


    public Class<?> getReferencedClass() {

        return referencedClasses.poll();
    }

    public boolean hasReferencedClass() {
        return referencedClasses.peek() != null;
    }

    public void setReferencedClass(Class<?> referencedClass) {
        referencedClasses.offer(referencedClass);
    }

}
