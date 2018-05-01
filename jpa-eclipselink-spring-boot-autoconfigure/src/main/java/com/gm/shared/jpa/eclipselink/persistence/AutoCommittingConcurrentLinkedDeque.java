package com.gm.shared.jpa.eclipselink.persistence;

import com.gm.shared.jpa.eclipselink.asyncpersistence.AsyncWriter;
import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentLinkedDeque;

@AllArgsConstructor
public class AutoCommittingConcurrentLinkedDeque<E> extends ConcurrentLinkedDeque<AsyncPersistenceObjectChangeSet> {

    private final AsyncWriter asyncWriter;
    private final JpaEclipseLinkProperties jpaEclipseLinkProperties;
    private final AsyncCommitService asyncCommitService;

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
