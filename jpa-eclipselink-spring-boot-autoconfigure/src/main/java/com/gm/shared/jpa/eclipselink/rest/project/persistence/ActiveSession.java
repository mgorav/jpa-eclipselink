package com.gm.shared.jpa.eclipselink.rest.project.persistence;

import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import static org.eclipse.persistence.jpa.JpaHelper.getServerSession;

@Component
@Transactional
public class ActiveSession {


    @Autowired
    private EntityManagerFactory emf;


    public Session getEmf() {
        return  getServerSession(EntityManagerFactoryInfo.class.cast(emf).getNativeEntityManagerFactory());
    }

}
