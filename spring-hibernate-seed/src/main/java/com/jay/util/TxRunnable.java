package com.jay.util;

import javax.persistence.EntityManager;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface TxRunnable {
	public void run(EntityManager manager) throws Exception;
}
