package com.gm.shared.jpa.eclipselink.asyncpersistence.em;


import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.eclipse.persistence.internal.sessions.UnitOfWorkChangeSet;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Map.Entry;

import static com.gm.shared.jpa.eclipselink.utils.CastUtil.uncheckedCast;
import static com.gm.shared.jpa.eclipselink.utils.Utils.nativeEM;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This handler provide the followings:-
 * 1. collect the UnitOfWorkChangeSet from the EM and release the producer thread without actually
 * performing the commit.
 * 2. It creates custom UnitOfWorkChangeSet and pushes ObjectChangeSet to it.
 */
@Component
public class ThreadBoundEntityManagerHandler {
    private static final Logger log = getLogger(ThreadBoundEntityManagerHandler.class);

    @SuppressWarnings("rawtypes")
    public AsyncPersistenceObjectChangeSet calculateChanges(EntityManager entityManager) {

        // This is required we are setting thread bound entity manager to NULL. In cased suspended exception, handler
        // might be called multiple times
        if (entityManager == null) {
            return null;
        }

        EntityManagerImpl queuedEntityManager = ((EntityManagerImpl) nativeEM(entityManager));
        // Create customized UnitOfWorkChangeSet and trigger calculate change set on it.
        // After that move all the newly added entities to the ObjectChangeSet of UnitOfWorkChangeSet
        Map allObjects = queuedEntityManager.getActivePersistenceContext(null).collectAndPrepareObjectsForNestedMerge();

        AbstractSession session = queuedEntityManager.getActivePersistenceContext(null);
        UnitOfWorkChangeSet uowChangeSet = new UnitOfWorkChangeSet(session);
        queuedEntityManager.getActivePersistenceContext(null).calculateChanges(allObjects, uowChangeSet, true, false);
        AsyncPersistenceObjectChangeSet changeSet = new AsyncPersistenceObjectChangeSet(uowChangeSet);
        if (log.isTraceEnabled()) {
            log.trace("Initial ChangeSet " + changeSet);
        }

        Map<Class, Map<ObjectChangeSet, ObjectChangeSet>> newCs = uowChangeSet.getNewObjectChangeSets();
        for (Entry<Class, Map<ObjectChangeSet, ObjectChangeSet>> newCsEntry : newCs.entrySet()) {
            Map<ObjectChangeSet, ObjectChangeSet> allObjCs = newCsEntry.getValue();
            for (Entry<ObjectChangeSet, ObjectChangeSet> allObjCsEntry : allObjCs.entrySet()) {
                // We are only interested in new objects as TL has created object change set for merged/updated/deleted entities
                // If we also add merged/updated/deleted entities this will end up in unique key constraint errors
                ObjectChangeSet objCs = allObjCsEntry.getKey();
                if (objCs.isNew()) {
                    uowChangeSet.addObjectChangeSet(objCs, session, false);
                }
            }
        }
        // Register the changes in AsyncPersistenceObjectChangeSet, which will be used to invalidate the JPA caches
        // locally plus all the nodes in the cluster
        // NOTE :- EL API getNewObjectsCloneToOriginal/getCloneMapping/getDeletedObjects is returning raw Map as a result
        // .entrySet returns Object
        // Collect new objects
        for (Object entry : queuedEntityManager.getActivePersistenceContext(null).getNewObjectsCloneToOriginal().entrySet()) {
            Entry<Object, Object> newObjsClonedToOriginalEntry = uncheckedCast(entry);
            Object newObj = newObjsClonedToOriginalEntry.getKey();
            changeSet.registerObjectChange(newObj.getClass(), session.getId(newObj));

        }
        // Collect modified objects
        for (Object entry : queuedEntityManager.getActivePersistenceContext(null).getCloneMapping().entrySet()) {
            Entry<Object, Object> updatedObjEntry = uncheckedCast(entry);
            Object updatedObj = updatedObjEntry.getKey();
            if (!queuedEntityManager.getActivePersistenceContext(null).getNewObjectsCloneToOriginal().containsKey(updatedObj)) {
                changeSet.registerObjectChange(updatedObj.getClass(), session.getId(updatedObj));
            }
        }
        // Collect deleted objects
        for (Object entry : queuedEntityManager.getActivePersistenceContext(null).getDeletedObjects().entrySet()) {
            Entry<Object, Object> deletedObjEntry = uncheckedCast(entry);
            Object deletedObj = deletedObjEntry.getKey();
            changeSet.registerObjectChange(deletedObj.getClass(), session.getId(deletedObj));
        }

        if (log.isTraceEnabled()) {
            log.trace("After Registration ChangeSet " + changeSet);
        }

        // release the queued entity manager and make it candidate for GC ASAP
        entityManager.clear();
        entityManager.close();
        entityManager = null;
        queuedEntityManager = null;

        return changeSet;

    }

}
