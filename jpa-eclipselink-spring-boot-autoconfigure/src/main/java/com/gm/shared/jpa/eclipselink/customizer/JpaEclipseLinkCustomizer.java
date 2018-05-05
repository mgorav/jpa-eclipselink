package com.gm.shared.jpa.eclipselink.customizer;

import org.eclipse.persistence.config.SessionCustomizer;
import org.springframework.core.Ordered;

public interface JpaEclipseLinkCustomizer extends SessionCustomizer, Ordered, Comparable<JpaEclipseLinkCustomizer> {
}
