package com.gm.shared.jpa.eclipselink.asyncpersistence.queue;

import com.gm.shared.jpa.eclipselink.asyncpersistence.AsyncWriter;
import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.asyncpersistence.persistence.AsyncCommitService;
import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;


public class AutoCommittingConcurrentLinkedDeque<E> extends ConcurrentLinkedDeque<AsyncPersistenceObjectChangeSet> {

    private final AsyncWriter asyncWriter;
    private final JpaEclipseLinkProperties jpaEclipseLinkProperties;
    private final AsyncCommitService asyncCommitService;
    private static Deque<AsyncPersistenceObjectChangeSet> deque;

    private AutoCommittingConcurrentLinkedDeque(AsyncWriter asyncWriter, JpaEclipseLinkProperties jpaEclipseLinkProperties, AsyncCommitService asyncCommitService) {
        this.asyncWriter = asyncWriter;
        this.jpaEclipseLinkProperties = jpaEclipseLinkProperties;
        this.asyncCommitService = asyncCommitService;

    }

    public static Deque<AsyncPersistenceObjectChangeSet> newDeque(AsyncWriter asyncWriter, JpaEclipseLinkProperties jpaEclipseLinkProperties, AsyncCommitService asyncCommitService) {

        if (deque == null) {
            deque = new AutoCommittingConcurrentLinkedDeque<>(asyncWriter, jpaEclipseLinkProperties, asyncCommitService);
        }

        return deque;
    }

    @Override
    public boolean offerFirst(AsyncPersistenceObjectChangeSet asyncPersistenceObjectChangeSet) {
        asyncCommitService.doCommit(this);
        return super.offerFirst(asyncPersistenceObjectChangeSet);
    }

    @Override
    public boolean offerLast(AsyncPersistenceObjectChangeSet asyncPersistenceObjectChangeSet) {
        asyncCommitService.doCommit(this);
        return super.offerLast(asyncPersistenceObjectChangeSet);
    }

    @Override
    public boolean offer(AsyncPersistenceObjectChangeSet asyncPersistenceObjectChangeSet) {
        asyncCommitService.doCommit(this);
        return super.offer(asyncPersistenceObjectChangeSet);
    }
}
