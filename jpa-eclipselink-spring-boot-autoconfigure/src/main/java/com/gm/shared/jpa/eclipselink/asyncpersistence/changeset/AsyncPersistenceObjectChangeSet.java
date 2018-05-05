package com.gm.shared.jpa.eclipselink.asyncpersistence.changeset;

import com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties;
import com.google.common.base.MoreObjects.ToStringHelper;
import org.eclipse.persistence.descriptors.VersionLockingPolicy;
import org.eclipse.persistence.internal.sessions.*;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.gm.shared.jpa.eclipselink.config.JpaEclipseLinkProperties.jpaEclipseLinkProperties;
import static com.google.common.base.MoreObjects.toStringHelper;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.UUID.randomUUID;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This contain all the {@link UnitOfWorkChangeSet} being used by thread bound EM. This call also memories list objects modified within
 * the transaction and its version number before batch committing
 */
public class AsyncPersistenceObjectChangeSet {

    private static final Logger log = getLogger(AsyncPersistenceObjectChangeSet.class);
    private final String asyncPersistenceObjectChangeSetId;
    private Map<Class<?>, Set<Object>> objectChangeSets;
    private UnitOfWorkChangeSet unitOfWorkChangeSet;
    private Map<Object, Long> allObjectsInTxByVersion;
    private JpaEclipseLinkProperties jpaEclipseLinkProperties;

    public UnitOfWorkChangeSet getUnitOfWorkChangeSet() {

        return unitOfWorkChangeSet;
    }

    public AsyncPersistenceObjectChangeSet(UnitOfWorkChangeSet unitOfWorkChangeSet) {

        // asyncPersistenceObjectChangeSetId is only enabled when tracing is enabled
        if (log.isTraceEnabled()) {
            asyncPersistenceObjectChangeSetId = randomUUID().toString();
        } else {
            asyncPersistenceObjectChangeSetId = "[only available if trace is enabled]";
        }
        // We just set the capacity of Map to some logically high number based on the commit size
        int commitCount = jpaEclipseLinkProperties().isPresent() ? jpaEclipseLinkProperties().get().getAsyncCommitCount() : 1;
        objectChangeSets = new HashMap<Class<?>, Set<Object>>(10 * commitCount);
        allObjectsInTxByVersion = new HashMap<Object, Long>(10 * commitCount);
        this.unitOfWorkChangeSet = unitOfWorkChangeSet;
        memorizeVersionsInUnitOfWorkChangeSet(unitOfWorkChangeSet);
    }

    /**
     * Revert to previous previous ovn if exception occurs while committing using AsyncWriter#batchCommit(java.util.List)
     * as entities in {@link UnitOfWorkChangeSet} is in underministic state
     */
    public AsyncPersistenceObjectChangeSet revertToObjectVersionBeforeBatchCommit() {

        Map<ObjectChangeSet, ObjectChangeSet> allChangeSet = unitOfWorkChangeSet.getAllChangeSets();
        AbstractSession session = unitOfWorkChangeSet.getSession();
        for (ObjectChangeSet objectChangeSet : allChangeSet.keySet()) {
            Object workingClone = objectChangeSet.getUnitOfWorkClone();
            VersionLockingPolicy versionLockingPolicy = (VersionLockingPolicy) session.getDescriptor(workingClone)
                    .getOptimisticLockingPolicy();
            doCorrectVersionInWorkingClone(session, objectChangeSet, workingClone, versionLockingPolicy);
        }

        for (ObjectChangeSet objectChangeSet : unitOfWorkChangeSet.getDeletedObjects().keySet()) {
            Object workingClone = objectChangeSet.getUnitOfWorkClone();
            VersionLockingPolicy versionLockingPolicy = (VersionLockingPolicy) session.getDescriptor(workingClone)
                    .getOptimisticLockingPolicy();
            doCorrectVersionInWorkingClone(session, objectChangeSet, workingClone, versionLockingPolicy);
        }

        return this;
    }

    public boolean hasChanges() {

        return unitOfWorkChangeSet.hasChanges();
    }

    /**
     * Register the objects in the tx, in order to remove L2 cache after batch commit
     */
    public void registerObjectChange(Class<?> aClass, Object pk) {

        if (!objectChangeSets.containsKey(aClass)) {
            objectChangeSets.put(aClass, new HashSet<Object>());
        }
        objectChangeSets.get(aClass).add(pk);
    }

    /**
     * Set an objects to be invalid in the L2 cache
     */
    public void evictCachesForAllObjectChanges() {

        for (Class<?> anEntityClass : objectChangeSets.keySet()) {
            Set<Object> pks = objectChangeSets.get(anEntityClass);
            for (Object pk : pks) {
                // true => the invalidation will be broadcast to each server in the cluster
                unitOfWorkChangeSet.getSession().getIdentityMapAccessor().invalidateObject(pk, anEntityClass, true);
            }

        }
    }


    @Override
    public String toString() {

        ToStringHelper toString = toStringHelper(this);
        toString.add("asyncPersistenceObjectChangeSetId", asyncPersistenceObjectChangeSetId);
        toString.add("hasChanges", unitOfWorkChangeSet.hasChanges());
        toString.add("hasDeletedObjects", unitOfWorkChangeSet.hasDeletedObjects());

        if (unitOfWorkChangeSet.hasChanges()) {
            Map<ObjectChangeSet, ObjectChangeSet> allChangeSet = unitOfWorkChangeSet.getAllChangeSets();
            AbstractSession session = unitOfWorkChangeSet.getSession();
            for (ObjectChangeSet objectChangeSet : allChangeSet.keySet()) {
                buildVersionToString(toString, session, objectChangeSet);
                buildRemainingAttributesToString(toString, objectChangeSet, FALSE);
            }
            for (ObjectChangeSet objectChangeSet : unitOfWorkChangeSet.getDeletedObjects().keySet()) {
                buildVersionToString(toString, session, objectChangeSet);
                buildRemainingAttributesToString(toString, objectChangeSet, TRUE);
            }
        }

        return toString.toString();

    }

    // ~~~~~~~~~~~~~~~~~~~~ Utility methods ~~~~~~~~~~~~~~~~~~~~

    private void buildVersionToString(ToStringHelper toString, AbstractSession session, ObjectChangeSet objectChangeSet) {

        if (objectChangeSet.getChanges().size() > 0) {
            Object workingCLone = objectChangeSet.getUnitOfWorkClone();
            VersionLockingPolicy versionLockingPolicy = (VersionLockingPolicy) session.getDescriptor(workingCLone)
                    .getOptimisticLockingPolicy();
            Long version = (Long) versionLockingPolicy.getVersionMapping().getAttributeValueFromObject(workingCLone);
            toString.add("version", version);
            toString.add("writeLockValue", objectChangeSet.getWriteLockValue());
        }
    }

    private void buildRemainingAttributesToString(ToStringHelper toString, ObjectChangeSet objectChangeSet, boolean isDelete) {

        if (objectChangeSet.getChanges().size() > 0) {
            toString.add("className", objectChangeSet.getClassName());
            toString.add("id", objectChangeSet.getId());
            toString.add("isNew", objectChangeSet.isNew());
            toString.add("isDelete", false);
        }
        for (ChangeRecord changeRecord : objectChangeSet.getChanges()) {
            toString.add("attributeName", changeRecord.getAttribute());
            toString.add("oldValue", changeRecord.getOldValue());

            if (DirectToFieldChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add("newValue", DirectToFieldChangeRecord.class.cast(changeRecord).getNewValue());
            } else if (AggregateChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add(AggregateChangeRecord.class.getSimpleName(), TRUE);
            } else if (AggregateCollectionChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add(AggregateCollectionChangeRecord.class.getSimpleName(), TRUE);
            } else if (CollectionChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add(CollectionChangeRecord.class.getSimpleName(), TRUE);
            } else if (CollectionChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add(CollectionChangeRecord.class.getSimpleName(), TRUE);
            } else if (DirectMapChangeRecord.class.isAssignableFrom(changeRecord.getClass())) {
                toString.add(DirectMapChangeRecord.class.getSimpleName(), TRUE);
            }
        }
    }

    private void memorizeVersionsInUnitOfWorkChangeSet(UnitOfWorkChangeSet unitOfWorkChangeSet) {

        Map<ObjectChangeSet, ObjectChangeSet> allChangeSet = unitOfWorkChangeSet.getAllChangeSets();
        AbstractSession session = unitOfWorkChangeSet.getSession();
        for (ObjectChangeSet objectChangeSet : allChangeSet.keySet()) {
            doMemorizeVersions(session, objectChangeSet);
        }
        // Required for deleted/removed objects
        for (ObjectChangeSet objectChangeSet : unitOfWorkChangeSet.getDeletedObjects().keySet()) {
            doMemorizeVersions(session, objectChangeSet);
        }
    }

    private void doMemorizeVersions(AbstractSession session, ObjectChangeSet objectChangeSet) {

        Object workingClone = objectChangeSet.getUnitOfWorkClone();
        VersionLockingPolicy versionLockingPolicy = (VersionLockingPolicy) session.getDescriptor(workingClone)
                .getOptimisticLockingPolicy();
        Long version = (Long) versionLockingPolicy.getVersionMapping().getAttributeValueFromObject(workingClone);
        allObjectsInTxByVersion.put(workingClone, version);

    }

    private void doCorrectVersionInWorkingClone(AbstractSession session, ObjectChangeSet objectChangeSet, Object workingClone,
                                                VersionLockingPolicy versionLockingPolicy) {

        if (allObjectsInTxByVersion.containsKey(workingClone)) {
            versionLockingPolicy.getVersionMapping().setAttributeValueInObject(workingClone,
                    allObjectsInTxByVersion.get(workingClone));
            doCorrectVersionInChangeRecord(objectChangeSet, workingClone, versionLockingPolicy);
        }
    }

    private void doCorrectVersionInChangeRecord(ObjectChangeSet objectChangeSet, Object workingClone,
                                                VersionLockingPolicy versionLockingPolicy) {

        for (ChangeRecord changeRecord : objectChangeSet.getChanges()) {
            if (changeRecord.getAttribute().equals(versionLockingPolicy.getVersionMapping().getAttributeName())) {
                DirectToFieldChangeRecord directToFieldChangeRecord = (DirectToFieldChangeRecord) changeRecord;
                directToFieldChangeRecord.setNewValue(allObjectsInTxByVersion.get(workingClone));
            }
        }
    }


}
