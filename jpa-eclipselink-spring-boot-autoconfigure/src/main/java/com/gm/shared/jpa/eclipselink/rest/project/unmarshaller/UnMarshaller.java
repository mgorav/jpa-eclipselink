package com.gm.shared.jpa.eclipselink.rest.project.unmarshaller;

import com.gm.shared.jpa.eclipselink.rest.project.ProjectService;
import org.eclipse.persistence.oxm.MediaType;
import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.oxm.XMLUnmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnMarshaller {

    @Autowired
    private ProjectService projectService;

    public XMLUnmarshaller unmarshaller(Class<?> aClass, MediaType mediaType) {


        XMLContext xmlContext = projectService.getXmlContext(aClass);

        XMLUnmarshaller unmarshaller = xmlContext.createUnmarshaller();
        unmarshaller.setMediaType(mediaType);
//         TODO   unmarshaller.setErrorHandler(new RestApiErrorHandler());
        if (unmarshaller.getMediaType().isApplicationJSON()) {
            unmarshaller.setWrapperAsCollectionName(true);
        }
        return unmarshaller;
    }
}
