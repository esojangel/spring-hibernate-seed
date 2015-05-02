package com.jay.util;

import javax.persistence.EntityManager;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface TxCallable<T> {
	public T call(EntityManager manager) throws Exception;
}
