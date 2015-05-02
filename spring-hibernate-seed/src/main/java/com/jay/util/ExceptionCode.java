package com.jay.util;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class ExceptionCode {
	public static final String OPTIMISTIC_LOCK="OPTIMISTIC_LOCK";
	
	/**
	 * {0} has client version of {1}. But it has been deleted";
	 */
	public static final String OPTIMISTIC_LOCK_DELETE = "OPTIMISTIC_LOCK_DELETE";
	
	/**
	 * Tenant security breach on {0}. Trying update other tenant data.
	 */
	public static final String TENANT_SECURITY = "TENANT_SECURITY";
	
	/**
	 * Tenant security breach on {0}. Current user is not super admin, trying update system data.
	 */
	public static final String ONLY_SUPER_ADMIN = "ONLY_SUPER_ADMIN";
	
	/**
	 * Cannot find the record in {0}.
	 */
	public static final String NO_ENTITY = "NO_ENTITY";
}
