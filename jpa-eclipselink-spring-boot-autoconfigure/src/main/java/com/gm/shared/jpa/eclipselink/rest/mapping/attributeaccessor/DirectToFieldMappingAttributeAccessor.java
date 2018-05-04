package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class DirectToFieldMappingAttributeAccessor extends AttributeAccessor {

    private final DirectToFieldMapping directToFieldMapping;
    private final AttributeAccessor attributeAccessor;

    public DirectToFieldMappingAttributeAccessor(DirectToFieldMapping directToFieldMapping) {
        this.directToFieldMapping = directToFieldMapping;
        this.attributeAccessor = directToFieldMapping.getAttributeAccessor();
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {
        return attributeAccessor.getAttributeValueFromObject(object);
    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        attributeAccessor.setAttributeValueInObject(object, value);
    }

}
