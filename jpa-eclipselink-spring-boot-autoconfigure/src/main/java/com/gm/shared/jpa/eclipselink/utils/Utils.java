package com.gm.shared.jpa.eclipselink.utils;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.sessions.Session;

import javax.persistence.EntityManager;

import static org.springframework.aop.support.AopUtils.*;

public class Utils {

    public static EntityManager nativeEM(EntityManager em) {

        if (isProxied(em)) {
            return em;
        }

        EntityManager retEM = (EntityManager) em.getDelegate();

        return retEM;

    }


    public static Session activeSession(EntityManager entityManager) {
        EntityManagerImpl emImpl = (EntityManagerImpl) nativeEM(entityManager);
        return emImpl.getActiveSession();
    }

    public static void waitFor(int timeInMills) {

        try {
            Thread.sleep(timeInMills);
        } catch (InterruptedException e) {
            // no worries .. as we were sleeping
        }
    }

    private static boolean isProxied(EntityManager em) {

        return isAopProxy(em) || isCglibProxy(em) || isJdkDynamicProxy(em);
    }

}