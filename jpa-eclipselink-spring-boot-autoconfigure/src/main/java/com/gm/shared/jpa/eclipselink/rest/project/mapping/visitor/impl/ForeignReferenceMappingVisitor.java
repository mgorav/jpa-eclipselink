package com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.impl;

import com.gm.shared.jpa.eclipselink.rest.project.mapping.attributeaccessor.ForeignReferenceMappingAttributeAccessor;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.visitor.MappingWeavingVisitor;
import com.gm.shared.jpa.eclipselink.rest.project.mapping.weaving.context.MappingWeavingContext;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.AttributeAccessor;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.oxm.XMLDescriptor;
import org.eclipse.persistence.oxm.mappings.XMLCompositeObjectMapping;
import org.eclipse.persistence.oxm.mappings.XMLInverseReferenceMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.gm.shared.jpa.eclipselink.utils.MappingUtil.biDirectionalMappedBy;
import static com.gm.shared.jpa.eclipselink.utils.Utils.activeSession;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ForeignReferenceMappingVisitor implements MappingWeavingVisitor<ForeignReferenceMapping> {

    @Lazy
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void visit(MappingWeavingContext<ForeignReferenceMapping> context) {
        ForeignReferenceMapping fkMapping = context.getCurrentDatabaseMapping();
        Class<?> refClass = fkMapping.getReferenceClass();
        String attributeName = fkMapping.getAttributeName();

        if (context.mappingDoesNotExistFor(attributeName)) {


            String biDirectionalMappedBy = biDirectionalMappedBy(fkMapping);

            if (!isEmpty(biDirectionalMappedBy)) {

                // Inverse mapping is created smartly. Consider an example: Let's say there are two entities
                // A, B which has one to many bi-directional relationship.
                // The aim is: when we start from A (root element), pointer of B pointing back to A should be an inverse mapping
                // but if B is a root element, we would like to create object reference mapping only (and bring only
                // direct attributes)
                XMLDescriptor refXmlCd = context.getCurrentXMLDescriptor(refClass);
                // Only create inverse mapping if in JPA bi-directional mapping exists (using biDirectionalMappedBy)
                // Bi-directional check we always start from ManyToOne/OneToOne side. If the xml descriptor (not JPA descriptor)
                // of bi-directional counter part reference descriptor contain mappings else
                // its a normal xml composite object mapping
                // (By default we always create "link" mapping, hence mapping exists check below take that into consideration
                if (refXmlCd != null) {
                    boolean shouldCreateInverseMapping = refXmlCd.getMappings().size() > 1 ? true : false;

                    if (shouldCreateInverseMapping) {
                        context.addMappingToCurrentXMLDescriptor(createXmlInverseReferenceMappingFrom(context, biDirectionalMappedBy));
                    } else {
                        context.addMappingToCurrentXMLDescriptor(createXmlObjectMappingFrom(context));
                    }
                }
            } else {
                context.addMappingToCurrentXMLDescriptor(createXmlObjectMappingFrom(context));
            }
        }
    }

    @Override
    public Class<ForeignReferenceMapping> getDatabaseMapping() {
        return ForeignReferenceMapping.class;
    }


    private XMLCompositeObjectMapping createXmlObjectMappingFrom(MappingWeavingContext<ForeignReferenceMapping> context) {

        ForeignReferenceMapping fkMapping = context.getCurrentDatabaseMapping();

        String attributeName = fkMapping.getAttributeName();

        final Class<?> refClass = fkMapping.getReferenceClass();
        final XMLCompositeObjectMapping compositeObjectMapping = new XMLCompositeObjectMapping();
        compositeObjectMapping.setAttributeAccessor(new ForeignReferenceMappingAttributeAccessor(fkMapping));

        compositeObjectMapping.setXPath(attributeName);
        compositeObjectMapping.setReferenceClass(refClass);


        return compositeObjectMapping;
    }


    /**
     * Create XMLInverseReferenceMapping for bi-directional mapping to avoid cycle in XML
     */
    private XMLInverseReferenceMapping createXmlInverseReferenceMappingFrom(MappingWeavingContext<ForeignReferenceMapping> context,
                                                                            String partnerInRelationshipAttributeName) {

        XMLInverseReferenceMapping xmlInverseRefMapping = new XMLInverseReferenceMapping();
        ForeignReferenceMapping fkMapping = context.getCurrentDatabaseMapping();
        String attributeName = fkMapping.getAttributeName();
        xmlInverseRefMapping.setAttributeAccessor(new AttributeAccessor() {

            private static final long serialVersionUID = -4419534966388894718L;

            @Override
            public void setAttributeValueInObject(Object object, Object value) throws DescriptorException {


                fkMapping.setRealAttributeValueInObject(object, value);
            }

            @Override
            public Object getAttributeValueFromObject(Object object) throws DescriptorException {

                return fkMapping.getRealAttributeValueFromObject(object, (AbstractSession) activeSession(entityManager));
            }

        });
        xmlInverseRefMapping.setAttributeName(attributeName);
        xmlInverseRefMapping.setReferenceClass(fkMapping.getReferenceClass());
        xmlInverseRefMapping.setMappedBy(partnerInRelationshipAttributeName);

        return xmlInverseRefMapping;
    }

}
