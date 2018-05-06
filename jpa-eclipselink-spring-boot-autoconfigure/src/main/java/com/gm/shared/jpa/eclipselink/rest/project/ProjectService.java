package com.gm.shared.jpa.eclipselink.rest.project;

import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.oxm.XMLMarshaller;
import org.eclipse.persistence.oxm.XMLUnmarshaller;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.eclipse.persistence.oxm.MediaType.APPLICATION_JSON;

@Component
public class ProjectService {
    private Map<Class<?>, XMLContext> xmlContexts;

    public Project newProject() {
        return new Project();
    }

    public void createXmlContext(Session session, Project project, Class<?> aClass) {

        if (xmlContexts == null) {
            xmlContexts = new HashMap<>(session.getDescriptors().size());
        }

        if (!xmlContexts.containsKey(aClass)) {
            xmlContexts.put(aClass, new XMLContext(project, session.getPlatform().getConversionManager().getLoader()));
        }
    }

    public XMLContext getXmlContext(Class<?> aClass) {
        return xmlContexts.get(aClass);
    }

    public boolean supportsClass(Class<?> aClass) {
        return xmlContexts.containsKey(aClass);
    }

    public XMLUnmarshaller unmarshaller(Class<?> aClass) {


        XMLContext xmlContext = getXmlContext(aClass);

        XMLUnmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setMediaType(APPLICATION_JSON);
//         TODO   unmarshaller.setErrorHandler(new RestApiErrorHandler());
        if (unmarshaller.getMediaType().isApplicationJSON()) {
            unmarshaller.setWrapperAsCollectionName(true);
        }
        return unmarshaller;
    }

    public XMLMarshaller xmlMarshaller(Class<?> aClass) {

        XMLContext xmlContext = getXmlContext(aClass);
        XMLMarshaller marshaller = xmlContext.createMarshaller();
        marshaller.setMediaType(APPLICATION_JSON);
        if (marshaller.getMediaType().isApplicationJSON()) {
            marshaller.setWrapperAsCollectionName(true);
        }

        return marshaller;
    }


}
