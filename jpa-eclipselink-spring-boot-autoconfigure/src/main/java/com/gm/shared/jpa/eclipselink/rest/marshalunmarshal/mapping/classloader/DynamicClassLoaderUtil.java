package com.gm.shared.jpa.eclipselink.rest.marshalunmarshal.mapping.classloader;

import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicClassWriter;
import org.eclipse.persistence.sessions.Session;

public abstract class DynamicClassLoaderUtil {

    public static DynamicClassLoader getDynamicClassLoader(Session session, DynamicClassLoader inUseDynamicClassLoader, Class<?> weavedInClass) {
        if (inUseDynamicClassLoader == null) {
            ClassLoader classLoader = session.getPlatform().getConversionManager().getLoader();
            if (DynamicClassLoader.class.isInstance(classLoader)) {
                if (weavedInClass != null) {
                    ((DynamicClassLoader) classLoader).defaultWriter = new DynamicClassWriter(weavedInClass);
                } else {
                    ((DynamicClassLoader) classLoader).defaultWriter = new DynamicClassWriter();
                }
            } else {
                classLoader = new DynamicClassLoader(session.getPlatform().getConversionManager().getLoader(), new DynamicClassWriter(weavedInClass));
            }

            return (DynamicClassLoader) classLoader;
        } else {
            return inUseDynamicClassLoader;
        }
    }
}
