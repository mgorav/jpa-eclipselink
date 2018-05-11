package com.gm.shared.jpa.eclipselink.rest.project.persistence;

import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

import static org.eclipse.persistence.jpa.JpaHelper.getServerSession;

@Component
public class ServerSession {


    @Autowired
    private EntityManagerFactory emf;


    public Session getEmf() {
        return getServerSession(EntityManagerFactoryInfo.class.cast(emf).getNativeEntityManagerFactory());
    }

}
