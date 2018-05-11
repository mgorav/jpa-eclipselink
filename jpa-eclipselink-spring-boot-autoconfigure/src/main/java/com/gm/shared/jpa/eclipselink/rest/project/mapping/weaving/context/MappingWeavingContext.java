package com.gm.shared.jpa.eclipselink.rest.project.mapping.weaving.context;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;

import java.util.ArrayDeque;
import java.util.Queue;


public class MappingWeavingContext<T extends DatabaseMapping> {
    private final Project project;
    private final ClassDescriptor currentClassDescriptor;
    private T currentDatabaseMapping;
    private final Queue<Class<?>> currentReferencedClasses;
    private final Session serverSession;

    public MappingWeavingContext(Session serverSession, Project project, ClassDescriptor currentClassDescriptor) {
        this.currentClassDescriptor = currentClassDescriptor;
        this.serverSession = serverSession;
        this.currentReferencedClasses = new ArrayDeque<>();
        this.project = project;
    }

    public Session getServerSession() {
        return serverSession;
    }

    public XMLDescriptor getXMLDescriptorFor(Class<?> aClass) {
        XMLDescriptor xmlDescriptor = (XMLDescriptor) project.getDescriptor(aClass);

        if (xmlDescriptor == null) {
            xmlDescriptor = newXMLDescriptor(aClass);
        }

        return xmlDescriptor;
    }

    public XMLDescriptor newXMLDescriptor(Class<?> aClass) {

        XMLDescriptor xmlDescriptor = new XMLDescriptor();
        xmlDescriptor.setDefaultRootElement(aClass.getSimpleName());
        xmlDescriptor.setJavaClass(aClass);
        xmlDescriptor.setFullyMergeEntity(true);
        project.addDescriptor(xmlDescriptor);

        return xmlDescriptor;
    }

    public ClassDescriptor getCurrentClassDescriptor() {
        return currentClassDescriptor;
    }

    public T getCurrentDatabaseMapping() {
        return currentDatabaseMapping;
    }

    public void setCurrentDatabaseMapping(T currentDatabaseMapping) {
        this.currentDatabaseMapping = currentDatabaseMapping;
    }

    public Class<?> getReferencedClass() {

        return currentReferencedClasses.poll();
    }

    public boolean hasReferencedClass() {
        return currentReferencedClasses.peek() != null;
    }

    public void setReferencedClass(Class<?> referencedClass) {
        currentReferencedClasses.offer(referencedClass);
    }

    public void addMapping(DatabaseMapping mapping) {
        getXMLDescriptorFor(currentClassDescriptor.getJavaClass()).addMapping(mapping);
    }

    public boolean mappingDoesNotExistFor(String attributeName) {

        return getXMLDescriptorFor(currentClassDescriptor.getJavaClass()).getMappingForAttributeName(attributeName) == null;
    }
}
