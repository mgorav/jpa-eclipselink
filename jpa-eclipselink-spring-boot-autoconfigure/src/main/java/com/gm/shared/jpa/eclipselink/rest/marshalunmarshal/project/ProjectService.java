package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.project;

import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.oxm.XMLMarshaller;
import org.eclipse.persistence.oxm.XMLUnmarshaller;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;
import static org.eclipse.persistence.oxm.MediaType.APPLICATION_JSON;

@Component
public class ProjectService {
    private Map<Class<?>, XMLContext> xmlContexts;
    private Map<Class<?>, JAXBContext> jaxbContexts;

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

        if (!xmlContexts.containsKey(aClass)) {
            // This means it's a not a JPA class. Let's create XMLContext
            // TODO
        }

        return xmlContexts.get(aClass);
    }

    public boolean supportsClass(Class<?> aClass) {
        return xmlContexts.containsKey(aClass);
    }

    public <T> T unmarshal(Class<?> aClass, InputStream inputStream) {


        XMLContext xmlContext = getXmlContext(aClass);

//        if (xmlContext != null) {

        XMLUnmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setMediaType(APPLICATION_JSON);
//         TODO   unmarshaller.setErrorHandler(new RestApiErrorHandler());
        unmarshaller.setWrapperAsCollectionName(true);
        unmarshaller.setIncludeRoot(false);
        return uncheckedCast(unmarshaller.unmarshal(inputStream, aClass));
//        }

//        if (!jaxbContexts.containsKey(aClass)) {
//
//            jaxbContexts.put(aClass, createContext(new Class[]{aClass}, null));
//
//        }
//
//        Unmarshaller unmarshaller = jaxbContexts.get(aClass).createUnmarshaller();
//        unmarshaller.setProperty(MEDIA_TYPE, APPLICATION_JSON);
//
//        return uncheckedCast(unmarshaller.unmarshal(inputStream));


    }

    public <T> void marshal(T object, OutputStream outputStream) {

        try {
            XMLContext xmlContext = getXmlContext(object.getClass());


            XMLMarshaller marshaller = xmlContext.createMarshaller();
            marshaller.setMediaType(APPLICATION_JSON);
            marshaller.setWrapperAsCollectionName(true);
            marshaller.setIncludeRoot(false);

            marshaller.marshal(object, outputStream);
        } catch (Exception exp) {
            // TODO propogate
            exp.printStackTrace();
        }
    }


}
