package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class OneToOneMappingAttributeAccessor extends AttributeAccessor {

    private final OneToManyMapping oneToManyMapping;
    private final AttributeAccessor attributeAccessor;

    public OneToOneMappingAttributeAccessor(OneToManyMapping oneToManyMapping) {
        this.oneToManyMapping = oneToManyMapping;
        this.attributeAccessor = oneToManyMapping.getAttributeAccessor();
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {
        return oneToManyMapping.getAttributeValueFromObject(object);
    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        oneToManyMapping.setAttributeValueInObject(object, value);
    }
}
