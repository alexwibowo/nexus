package org.isolution.nexus.test.support;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * User: Alex Wibowo
 * Date: 18/08/11
 * Time: 12:34 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractTransactionalTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected SessionFactory sessionFactory;


    protected void commitAndRenewTransaction() {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.getTransaction();
        transaction.commit();


        Transaction newTransaction = currentSession.beginTransaction();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        sessionHolder.setTransaction(newTransaction);
    }
}
