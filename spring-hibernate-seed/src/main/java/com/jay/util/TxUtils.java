package com.jay.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class TxUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(TxUtils.class);
	
	public static EntityManager getEntityManager() {
		EntityManagerFactory factory=ContextUtils.getBean("entityManagerFactory");
		return factory.createEntityManager();
	}
	
	public static <T> T callTx(TxCallable<T> callable) {
		EntityManager manager = TxUtils.getEntityManager();
		EntityTransaction tx = null;
		T result = null;
		try {
			tx = manager.getTransaction();
			tx.begin();
			result = callable.call(manager);
			tx.commit();
		}
		catch (Throwable e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			LOGGER.error("Transaction failed:", e);
			throw new RuntimeException(e);
		}
		finally {
			manager.close();
		}

		return result;
	}
	
	public static void runTx(TxRunnable runnable) {
		EntityManager manager = TxUtils.getEntityManager();
		EntityTransaction tx = null;

		try {
			tx = manager.getTransaction();
			tx.begin();
			runnable.run(manager);
			tx.commit();
		}
		catch (Throwable e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			LOGGER.error("Transaction failed:", e);
		}
		finally {
			manager.close();
		}
	}
}