package com.gm.shared.jpa.eclipselink.rest.mapping.context;

import lombok.Data;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.sessions.Project;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;


@Data
public class MappingWeavingContext<T extends DatabaseMapping> {
    private final Project project;
    private final ClassDescriptor classDescriptor;
    private final XMLDescriptor xmlDescriptor;
    private T databaseMapping;
    private final Map<Class<?>, MappingWeavingArtifact> metadata;
    private final Queue<Class<?>> referencedClasses;

    public MappingWeavingContext(ClassDescriptor classDescriptor) {
        this.classDescriptor = classDescriptor;
        this.metadata = new HashMap<>();
        this.referencedClasses = new ArrayDeque<>();
        this.project = new Project();
        this.xmlDescriptor = new XMLDescriptor();
        this.project.addDescriptor(this.xmlDescriptor);
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
