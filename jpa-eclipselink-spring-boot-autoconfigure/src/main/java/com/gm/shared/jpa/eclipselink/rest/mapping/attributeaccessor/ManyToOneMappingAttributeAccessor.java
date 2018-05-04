package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class ManyToOneMappingAttributeAccessor extends AttributeAccessor {
    private final ManyToOneMapping manyToOneMapping;
    private final AttributeAccessor attributeAccessor;

    public ManyToOneMappingAttributeAccessor(ManyToOneMapping manyToOneMapping) {
        this.manyToOneMapping = manyToOneMapping;
        this.attributeAccessor = manyToOneMapping.getAttributeAccessor();
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
