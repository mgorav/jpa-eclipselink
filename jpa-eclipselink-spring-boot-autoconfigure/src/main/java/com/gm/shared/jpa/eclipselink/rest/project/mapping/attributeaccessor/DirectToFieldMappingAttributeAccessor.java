package com.gm.shared.jpa.eclipselink.rest.project.mapping.attributeaccessor;

import com.gm.shared.jpa.eclipselink.rest.project.persistence.ActiveSession;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

import static com.gm.shared.jpa.eclipselink.autoconfigure.BeanLocator.beanOfType;

@Configurable(autowire = Autowire.BY_TYPE, dependencyCheck = true, preConstruction = true)
public class DirectToFieldMappingAttributeAccessor extends AttributeAccessor {

    private DirectToFieldMapping directToFieldMapping;
    private AttributeAccessor attributeAccessor;
    private Converter converter;


    public DirectToFieldMappingAttributeAccessor(DirectToFieldMapping directToFieldMapping) {
        this.directToFieldMapping = directToFieldMapping;
        this.attributeAccessor = directToFieldMapping.getAttributeAccessor();
        this.converter = directToFieldMapping.getConverter();
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        Session session = beanOfType(ActiveSession.class).getEmf();
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
        Session session = beanOfType(ActiveSession.class).getEmf();

        if (converter != null) {
            value = converter.convertDataValueToObjectValue(value, session);
        } else {
            value = session.getDatasourcePlatform().convertObject(value, directToFieldMapping.getAttributeClassification());
        }

        directToFieldMapping.setRealAttributeValueInObject(object, value);
    }

}
