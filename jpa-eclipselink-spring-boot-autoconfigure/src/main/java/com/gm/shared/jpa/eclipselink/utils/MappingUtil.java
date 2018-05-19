package com.gm.shared.jpa.eclipselink.utils;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.ForeignReferenceMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;

public class MappingUtil {

    public static String biDirectionalMappedBy(ForeignReferenceMapping fkMapping) {

        // Inverse mapping should be created on the child side (for the parent)
        // - Target foreign keys are owned by the child, not
        // the parent
        if (fkMapping.isOneToOneMapping()
                && OneToOneMapping.class.cast(fkMapping).getForeignKeyFieldNames().size() == 0) {
            return null;
        } else if (fkMapping.isOneToManyMapping()) {
            return null;
        } else if (fkMapping.getReferenceDescriptor() == null)  {
            return null;
        }

        return doGetBiDirectionalMappedBy(fkMapping.getDescriptor().getJavaClass(), fkMapping.getReferenceDescriptor());

    }

    /**
     * Return relationship in partner name or null
     */
    private static String doGetBiDirectionalMappedBy(Class<?> sourceClass, ClassDescriptor referenceDescriptor) {

        // Loop over reference class descriptor mapping and find out if there
        // OneToMany/OneToMany relationship pointing to
        // sourceClass. IF yes, then its a bi-directional relationship
        for (DatabaseMapping databaseMapping : referenceDescriptor.getMappings()) {
            if (databaseMapping.isOneToManyMapping()
                    || (!databaseMapping.isManyToOneMapping() && databaseMapping.isOneToOneMapping())) {
                if (sourceClass.isAssignableFrom(ForeignReferenceMapping.class.cast(databaseMapping).getReferenceClass())) {


                    return databaseMapping.getAttributeName();
                }
            }
        }

        return null;
    }
}
