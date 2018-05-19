package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.attributeaccessor;

import com.gm.shared.jpa.eclipselink.rest.persistence.JpaService;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Configurable;

import static com.gm.shared.jpa.eclipselink.autoconfigure.BeanLocator.beanOfType;
import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;

@Configurable
/**
 * For ManyToOne and OneToOne
 */
public class ForeignReferenceMappingAttributeAccessor extends AttributeAccessor {
    private final ForeignReferenceMapping fkMapping;


    public ForeignReferenceMappingAttributeAccessor(ForeignReferenceMapping fkMapping) {
        this.fkMapping = fkMapping;
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        JpaService jpaService = beanOfType(JpaService.class);

        Session session = (AbstractSession) activeSession(jpaService.getEM());

        session = session == null ? jpaService.getEmf() : session;

        return fkMapping.getRealAttributeValueFromObject(object, (AbstractSession) session);

    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {
        fkMapping.setRealAttributeValueInObject(object, value);
    }
}
