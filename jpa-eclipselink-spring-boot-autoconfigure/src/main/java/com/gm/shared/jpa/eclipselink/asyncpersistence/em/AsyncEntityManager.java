package com.gm.shared.jpa.eclipselink.asyncpersistence.em;

import com.gm.shared.jpa.eclipselink.asyncpersistence.changeset.AsyncPersistenceObjectChangeSet;
import com.gm.shared.jpa.eclipselink.errorhandling.ErrorHandler;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.UnitOfWorkChangeSet;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.gm.shared.jpa.eclipselink.utils.Utils.nativeEM;
import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

/**
 * The service takes up the following responsibilities:-
 * 1. Perform the batch commit using list of AsyncPersistenceObjectChangeSet
 * 2. It first collates all the UnitOfWorkChangeSet in one and then merges that UnitOfWorkChangeSet to the active EM
 * 3.It pushes UnitOfWorkChangeSet via RepeatableUnitOfWork to CommandManager to resolve change set conflicts
 * and make them available in the current transaction
 * 4. Also provide AsyncPersistenceObjectChangeSet (one at time) commit using individualCommit. This method will be triggered in
 * case of an error (exception scenario)
 */
@Component
public class AsyncEntityManager {

    private static final Logger log = getLogger(AsyncEntityManager.class);
    @PersistenceContext
    private EntityManager activeEntityManager;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired(required = false)
    private ErrorHandler errorHandler;

    /**
     * This method uses batch transaction manager to commit {@link AsyncPersistenceObjectChangeSet} to the db and also evict the
     * L2 cache for the thread bound EM. It will only evict the objects being modified/delete
     */
    public void batchCommit(List<AsyncPersistenceObjectChangeSet> asyncChangeSets) {

        if (log.isTraceEnabled()) {
            log.trace(String.format("Called for %d changes", asyncChangeSets.size()));
        }
        // We are using manual transaction for two reasons-
        // 1. Avoid flushing by JpaTranactionInterceptor
        // 2. Firing of the validation as validation are fired from JpaTransactionInterceptor

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // explicitly setting the transaction name is something that can only be done programmatically
        def.setName(this.getClass().getName());
        def.setPropagationBehavior(PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {

            // Get an active EM and merge UnitOfWorkChangeSet to it. This will avoid any extra querying while merging.
            EntityManagerImpl nativeActiveEntityManager = (EntityManagerImpl) nativeEM(activeEntityManager);
            // Create a UnitOfWorkChangeSet while will used to collate all the UnitOfWorkChangeSets collected in the queue
            UnitOfWorkChangeSet activeEMUnitOfWorkChangeSet = (UnitOfWorkChangeSet) nativeActiveEntityManager.getActivePersistenceContext(null).getCurrentChanges();

            AbstractSession session = nativeActiveEntityManager.getActivePersistenceContext(null);

            for (AsyncPersistenceObjectChangeSet asyncChangeSet : asyncChangeSets) {
                try {
                    // Merge the collected UnitOfWorkChangeSet to active EM UnitOfWorkChangeSet
                    activeEMUnitOfWorkChangeSet.mergeUnitOfWorkChangeSet(asyncChangeSet.getUnitOfWorkChangeSet(), session, false);
                } catch (Exception exp) {
                    // back out if there is any exception while merging
                    log.error("Error while mergeUnitOfWorkChangeSet", exp);
                    throw exp;
                }
                asyncChangeSet.evictCachesForAllObjectChanges();
            }
            // push the collated UnitOfWorkChangeSet to commit manager to handle conflicts plus make them particiable
            // in the current running transaction
            nativeActiveEntityManager.getActivePersistenceContext(null).setUnitOfWorkChangeSet(activeEMUnitOfWorkChangeSet);

        } catch (RuntimeException ex) {

            status.setRollbackOnly();
            throw ex;

        } finally {
            transactionManager.commit(status);
        }

    }

    /**
     * AsyncPersistenceObjectChangeSet one at time commit in case of error and pass the exception to the exception handler
     */
    public void individualCommit(AsyncPersistenceObjectChangeSet changeSet) {


        Exception individualCommitException = null;
        try {
            // Before going to row by row commit revert to ovn before batch commit. This done because when exception occurs
            // objects are in underministic state
            changeSet.revertToObjectVersionBeforeBatchCommit();
            batchCommit(asList(changeSet));
        } catch (Exception e) {
            individualCommitException = e;
            log.error("Error executing asynchronous commits in individual unit of work transactions", e);
        }

        if (individualCommitException != null && errorHandler != null) {
            errorHandler.handle(individualCommitException);
        }

    }
}
