package com.gm.shared.jpa.eclipselink.asyncpersistence.persistence;

import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.asyncpersistence.em.AsyncEntityManager;
import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.gm.shared.jpa.eclipselink.asyncpersistence.queue.AutoCommittingConcurrentLinkedDeque.newDeque;
import static com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties.jpaEclipseLinkProperties;

@Component
public class AsyncCommitService {

    @Autowired
    @Lazy
    private AsyncEntityManager asyncEntityManager;
    private JpaEclipseLinkProperties jpaEclipseLinkProperties;
    @Autowired
    @Lazy
    private AsyncCommitService asyncCommitService;

    @Async
    public void doCommit(Deque<AsyncPersistenceObjectChangeSet> queue) {

        if (queue != null) {
            JpaEclipseLinkProperties jpaEclipseLinkProperties = jpaEclipseLinkProperties().get();
            int size = queue.size();
            if (size == jpaEclipseLinkProperties.getAsyncCommitCount()) {
                List<AsyncPersistenceObjectChangeSet> changeSetList = new ArrayList<>(size);

                AsyncPersistenceObjectChangeSet changeSet = queue.poll();

                while (changeSet != null) {
                    changeSetList.add(changeSet);

                    changeSet = queue.poll();
                }

                if (!changeSetList.isEmpty()) {
                    asyncEntityManager.batchCommit(changeSetList);
                }

            }
        }
    }

    @Scheduled(fixedDelay = 1000000)
    public void doCommit() {
        doCommit(newDeque(asyncEntityManager, jpaEclipseLinkProperties, asyncCommitService));
    }


}
