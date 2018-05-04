package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class OneToManyMappingAttributeAccessor extends AttributeAccessor{
    private final OneToManyMapping oneToManyMapping;
    private final AttributeAccessor attributeAccessor;

    public OneToManyMappingAttributeAccessor(OneToManyMapping oneToManyMapping) {
        this.oneToManyMapping = oneToManyMapping;
        this.attributeAccessor = oneToManyMapping.getAttributeAccessor();
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
