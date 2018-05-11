package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.attributeaccessor;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.queries.ContainerPolicy;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Map;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.castToList;
import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;

@Configurable
public class OneToManyMappingAttributeAccessor extends AttributeAccessor {
    private final OneToManyMapping oneToManyMapping;
    private final AttributeAccessor attributeAccessor;
    @PersistenceContext
    private EntityManager entityManager;

    public OneToManyMappingAttributeAccessor(OneToManyMapping oneToManyMapping) {
        this.oneToManyMapping = oneToManyMapping;
        this.attributeAccessor = oneToManyMapping.getAttributeAccessor();
    }

    @Override
    public Object getAttributeValueFromObject(Object object) throws DescriptorException {

        Object retObj = oneToManyMapping.getRealAttributeValueFromObject(object, (AbstractSession) activeSession(entityManager));
        ContainerPolicy containerPolicy = oneToManyMapping.getContainerPolicy();
        retObj = castToList(!Map.class.isAssignableFrom(containerPolicy.getContainerClass()) ?
                retObj : new ArrayList<>(Map.class.cast(retObj).values()));

        return retObj;
    }

    @Override
    public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {

        attributeAccessor.setAttributeValueInObject(object, value);
    }
}
