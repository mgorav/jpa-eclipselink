package com.gm.shared.jpa.eclipselink.customizer.controller;

import com.gm.shared.jpa.eclipselink.customizer.JpaEclipseLinkCustomizer;
import com.gm.shared.jpa.eclipselink.customizer.exception.JpaEclipseCustomizationFailedException;
import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.List;

import static com.gm.shared.jpa.eclipselink.autoconfigure.BeanLocator.allBeansOfType;
import static java.util.Collections.sort;
import static org.slf4j.LoggerFactory.getLogger;

@Configurable
public class JpaEclipseLinkControllerCustomizer implements SessionCustomizer {
    private static final Logger log = getLogger(JpaEclipseLinkControllerCustomizer.class);
    private List<JpaEclipseLinkCustomizer> customizers = allBeansOfType(JpaEclipseLinkCustomizer.class);


    public JpaEclipseLinkControllerCustomizer() {

    }

    @Override
    public void customize(Session session) throws Exception {

        sort(customizers);
        customizers.forEach(customizer -> {
            try {
                customizer.customize(session);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new JpaEclipseCustomizationFailedException(e);
            }
        });


    }
}
