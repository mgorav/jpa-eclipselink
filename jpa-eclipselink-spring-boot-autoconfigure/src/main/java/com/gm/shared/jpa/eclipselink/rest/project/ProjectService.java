package com.gm.shared.jpa.eclipselink.rest.project;

import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProjectService {
    private Map<Class<?>, XMLContext> allJpaXmlContexts;

    public Project newProject() {
        return new Project();
    }

    public void createXmlContext(Session session, Project project, Class<?> aClass) {

        if (allJpaXmlContexts == null) {
            allJpaXmlContexts = new HashMap<>(session.getDescriptors().size());
        }

        if (!allJpaXmlContexts.containsKey(aClass)) {
            allJpaXmlContexts.put(aClass, new XMLContext(project, session.getPlatform().getConversionManager().getLoader()));
        }
    }

    public XMLContext getXmlContext(Class<?> aClass) {
        return allJpaXmlContexts.get(aClass);
    }


}
