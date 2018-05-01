package com.gm.shared.jpa.eclipselink.txmgr;

import com.gm.shared.jpa.eclipselink.asyncpersistence.AsyncWriter;
import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.asyncpersistence.em.ThreadBoundEntityManagerHandler;
import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import com.gm.shared.jpa.eclipselink.persistence.AsyncCommitService;
import com.gm.shared.jpa.eclipselink.persistence.AutoCommittingConcurrentLinkedDeque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Queue;

import static com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties.jpaEclipseLinkProperties;

@Component
public class SmartJpaTransactionManager extends JpaTransactionManager {
    @PersistenceContext
    private EntityManager entityManager;
    private Queue<AsyncPersistenceObjectChangeSet> unitOfWorkChangeSetQueue;
    @Autowired(required = false)
    private ThreadBoundEntityManagerHandler threadBoundEMHandler;
    @Autowired(required = false)
    private AsyncWriter asyncWriter;
    @Autowired(required = false)
    private AsyncCommitService commitService;

    @Override
    protected void doCommit(DefaultTransactionStatus status) {

        if (jpaEclipseLinkProperties().isPresent()) {
            JpaEclipseLinkProperties jpaEclipseLinkProperties = jpaEclipseLinkProperties().get();

            // collect & clear
            AsyncPersistenceObjectChangeSet changeSet = threadBoundEMHandler.calculateChanges(entityManager);

            if (changeSet != null) {
                if (unitOfWorkChangeSetQueue.size() <= jpaEclipseLinkProperties.getAsyncCommitCount()) {
                    unitOfWorkChangeSetQueue.offer(changeSet);
                }
            }

        }

        super.doCommit(status);
    }


    @PostConstruct
    public void postConstruct() {
        if (jpaEclipseLinkProperties().isPresent()) {
            unitOfWorkChangeSetQueue = new AutoCommittingConcurrentLinkedDeque<>(asyncWriter, jpaEclipseLinkProperties().get(),commitService);
        }
    }
}
