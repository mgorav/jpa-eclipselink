package com.gm.shared.jpa.eclipselink.rest.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.EntityManager;

import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;

@Configurable
public class ManyToOneMappingAttributeAccessor extends AttributeAccessor {
    private final ForeignReferenceMapping fkMapping;
    @Autowired
    private EntityManager em;

    public ManyToOneMappingAttributeAccessor(ForeignReferenceMapping fkMapping) {
        this.fkMapping = fkMapping;
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        return fkMapping.getRealAttributeValueFromObject(object, (AbstractSession) activeSession(em));

    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        fkMapping.setRealAttributeValueInObject(object, value);
    }
}
