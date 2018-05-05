package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.ManyToOneMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.EntityManager;

import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;

@Configurable
public class ManyToOneMappingAttributeAccessor extends AttributeAccessor {
    private final ManyToOneMapping manyToOneMapping;
    @Autowired
    private EntityManager em;

    public ManyToOneMappingAttributeAccessor(ManyToOneMapping manyToOneMapping) {
        this.manyToOneMapping = manyToOneMapping;
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        return manyToOneMapping.getRealAttributeValueFromObject(object, (AbstractSession) activeSession(em));

    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        manyToOneMapping.setRealAttributeValueInObject(object, value);
    }
}
