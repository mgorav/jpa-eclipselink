package com.gm.shared.jpa.eclipselink.txmgr;

import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.asyncpersistence.em.AsyncEntityManager;
import com.gm.shared.jpa.eclipselink.asyncpersistence.em.ThreadBoundEntityManagerHandler;
import com.gm.shared.jpa.eclipselink.asyncpersistence.persistence.AsyncCommitService;
import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Queue;

import static com.gm.shared.jpa.eclipselink.asyncpersistence.queue.AutoCommittingConcurrentLinkedDeque.newDeque;
import static com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties.jpaEclipseLinkProperties;

public class SmartJpaTransactionManager extends JpaTransactionManager {
    @PersistenceContext
    private EntityManager entityManager;
    private Queue<AsyncPersistenceObjectChangeSet> unitOfWorkChangeSetQueue;
    @Autowired(required = false)
    private ThreadBoundEntityManagerHandler threadBoundEMHandler;
    @Autowired(required = false)
    private AsyncEntityManager asyncEntityManager;
    @Autowired(required = false)
    private AsyncCommitService commitService;

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        handleAsyncPersistence();
        super.prepareForCommit(status);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {

        handleAsyncPersistence();

        super.doCommit(status);
    }

    private void handleAsyncPersistence() {
        if (jpaEclipseLinkProperties().isPresent()) {
            JpaEclipseLinkProperties jpaEclipseLinkProperties = jpaEclipseLinkProperties().get();

            // collect & clear
            AsyncPersistenceObjectChangeSet changeSet = threadBoundEMHandler.calculateChanges(entityManager);

            if (changeSet != null) {
                unitOfWorkChangeSetQueue.offer(changeSet);
            }

        }
    }


    @PostConstruct
    public void postConstruct() {
        if (jpaEclipseLinkProperties().isPresent()) {
            unitOfWorkChangeSetQueue = newDeque(asyncEntityManager, jpaEclipseLinkProperties().get(), commitService);
        }
    }
}
