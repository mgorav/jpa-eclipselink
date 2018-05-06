package com.gm.shared.jpa.eclipselink.rest.project.marshaller;

import com.gm.shared.jpa.eclipselink.rest.project.ProjectService;
import org.eclipse.persistence.oxm.MediaType;
import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.oxm.XMLMarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMarshaller {

    @Autowired
    private ProjectService projectService;

    public XMLMarshaller xmlMarshaller(Class<?> aClass, MediaType mediaType) {

        XMLContext xmlContext = projectService.getXmlContext(aClass);
        XMLMarshaller marshaller = xmlContext.createMarshaller();
        marshaller.setMediaType(mediaType);
        if (marshaller.getMediaType().isApplicationJSON()) {
            marshaller.setWrapperAsCollectionName(true);
        }

        return marshaller;
    }
}

