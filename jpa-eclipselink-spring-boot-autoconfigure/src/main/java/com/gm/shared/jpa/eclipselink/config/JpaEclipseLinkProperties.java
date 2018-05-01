package com.gm.shared.jpa.eclipselink.config;

import lombok.Getter;

import java.util.Optional;

import static java.util.Optional.of;

@Getter
public class JpaEclipseLinkProperties {

    private int asyncCommitCount;
    private boolean asyncPersistence;
    private static JpaEclipseLinkProperties jpaEclipseLinkProperties;

    private JpaEclipseLinkProperties(int asyncCommitCount, boolean asyncPersistence) {
        this.asyncCommitCount = asyncCommitCount;
        this.asyncPersistence = asyncPersistence;
    }

    public static JpaEclipseLinkProperties newJpaEclipseLinkProperties(int asyncCommitCount) {
        if (jpaEclipseLinkProperties == null) {
            jpaEclipseLinkProperties = new JpaEclipseLinkProperties(asyncCommitCount,true);
        }
        return jpaEclipseLinkProperties;
    }

    public static Optional<JpaEclipseLinkProperties> jpaEclipseLinkProperties() {

        return of(jpaEclipseLinkProperties);
    }
}
