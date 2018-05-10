package com.gm.shared.jpa.eclipselink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class JpaEclipseLinkProperties {

    private int asyncCommitCount;
    private boolean asyncPersistence;
    private boolean enableEclipseLinkProfiling;
    private static JpaEclipseLinkProperties jpaEclipseLinkProperties;

    private JpaEclipseLinkProperties() {
    }

    public int getAsyncCommitCount() {
        return asyncCommitCount;
    }

    public boolean isAsyncPersistence() {
        return asyncPersistence;
    }

    public void setAsyncCommitCount(int asyncCommitCount) {
        this.asyncCommitCount = asyncCommitCount;
        asyncPersistence = true;
    }

    public void setAsyncPersistence(boolean asyncPersistence) {
        this.asyncPersistence = asyncPersistence;
    }

    public boolean isEnableEclipseLinkProfiling() {
        return enableEclipseLinkProfiling;
    }

    public void setEnableEclipseLinkProfiling(boolean enableEclipseLinkgProfiling) {
        this.enableEclipseLinkProfiling = enableEclipseLinkgProfiling;
    }

    public static JpaEclipseLinkProperties newJpaEclipseLinkProperties() {
        if (jpaEclipseLinkProperties == null) {
            jpaEclipseLinkProperties = new JpaEclipseLinkProperties();
        }
        return jpaEclipseLinkProperties;
    }

    public static Optional<JpaEclipseLinkProperties> jpaEclipseLinkProperties() {

        return ofNullable(jpaEclipseLinkProperties);
    }
}
