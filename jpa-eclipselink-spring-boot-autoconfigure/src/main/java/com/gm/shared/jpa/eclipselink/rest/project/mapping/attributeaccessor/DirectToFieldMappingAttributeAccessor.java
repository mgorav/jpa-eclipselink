package com.gm.shared.jpa.eclipselink.rest.project.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;

@Configurable
public class DirectToFieldMappingAttributeAccessor extends AttributeAccessor {

    private final DirectToFieldMapping directToFieldMapping;
    private final AttributeAccessor attributeAccessor;
    private final Converter converter;

    @PersistenceContext
    private EntityManager entityManager;

    public DirectToFieldMappingAttributeAccessor(DirectToFieldMapping directToFieldMapping) {
        this.directToFieldMapping = directToFieldMapping;
        this.attributeAccessor = directToFieldMapping.getAttributeAccessor();
        this.converter = directToFieldMapping.getConverter();
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        Session session = activeSession(entityManager);
        Object retVal = directToFieldMapping.getRealAttributeValueFromObject(object, (AbstractSession) session);

        if (converter != null) {
            retVal = converter.convertObjectValueToDataValue(retVal, session);
        } else {
            retVal = session.getDatasourcePlatform().convertObject(retVal, directToFieldMapping.getAttributeClassification());
        }

        return retVal;
    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        Session session = activeSession(entityManager);

        if (converter != null) {
            value = converter.convertDataValueToObjectValue(value, session);
        } else {
            value = session.getDatasourcePlatform().convertObject(value, directToFieldMapping.getAttributeClassification());
        }

        directToFieldMapping.setRealAttributeValueInObject(object, value);
    }

}
