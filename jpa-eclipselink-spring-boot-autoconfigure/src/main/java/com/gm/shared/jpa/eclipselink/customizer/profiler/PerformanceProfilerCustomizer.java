package com.gm.shared.jpa.eclipselink.customizer.profiler;

import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import com.gm.shared.jpa.eclipselink.customizer.JpaEclipseLinkCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.profiler.PerformanceProfiler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties.jpaEclipseLinkProperties;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.valueOf;

public class PerformanceProfilerCustomizer implements JpaEclipseLinkCustomizer {

    @Override
    public void customize(Session session) throws Exception {

        Optional<JpaEclipseLinkProperties> properties = jpaEclipseLinkProperties();

        if (properties.isPresent() && properties.get().isEnableEclipseLinkProfiling()) {
            PerformanceProfiler performanceProfiler = new PerformanceProfiler();
            performanceProfiler.logProfileSummaryByClass();
            performanceProfiler.logProfile();
            session.setProfiler(performanceProfiler);
        }

    }

    @Override
    public int getOrder() {
        return MAX_VALUE - 1;
    }

    @Override
    public int compareTo(JpaEclipseLinkCustomizer o) {

        return valueOf(o.getOrder()).compareTo(valueOf(this.getOrder()));
    }
}