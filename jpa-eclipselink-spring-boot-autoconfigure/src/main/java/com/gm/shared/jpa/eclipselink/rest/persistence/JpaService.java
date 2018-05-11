package com.gm.shared.jpa.eclipselink.rest.persistence;

import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import static org.eclipse.persistence.jpa.JpaHelper.getServerSession;

@Component
public class JpaService {


    @Autowired
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager entityManager;


    public Session getEmf() {
        return getServerSession(EntityManagerFactoryInfo.class.cast(emf).getNativeEntityManagerFactory());
    }

    public EntityManager getEM() {
        return entityManager;
    }

}
