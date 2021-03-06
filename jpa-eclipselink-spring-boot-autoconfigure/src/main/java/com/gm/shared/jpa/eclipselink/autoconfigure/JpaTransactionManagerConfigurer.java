package com.gm.shared.jpa.eclipselink.autoconfigure;

import com.gm.shared.jpa.eclipselink.txmgr.SmartJpaTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

/**
 * This class transaction manager to be used by JPA/Eclipselink
 */
@Configuration
public class JpaTransactionManagerConfigurer {

    private boolean asyncPersistence;

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        final SmartJpaTransactionManager transactionManager = new SmartJpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
