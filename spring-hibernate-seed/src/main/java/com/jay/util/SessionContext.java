package com.jay.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class SessionContext implements java.io.Serializable{

	private static final long serialVersionUID = 5485964377060268572L;

	private final static ThreadLocal<SessionContext> context = new ThreadLocal<SessionContext>();
	
	private String sessionUUID;
	
	private int userId;
	
	private int tenantId;
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public static void setContext(SessionContext ctx) {
		context.set(ctx);
		MDC.clear();
		if(ctx != null) {
			MDC.put("session", ctx.sessionUUID);
			MDC.put("tenant", Integer.toString(ctx.getTenantId()));
			MDC.put("user", Integer.toString(ctx.getUserId()));
		}
	}

	public static SessionContext getContext() {
		return context.get();
	}

	public static void removeContext() {
		context.remove();
		MDC.clear();
	}

	public SessionContext() {
		
	}
	
	public SessionContext(String sessionUUID, int userId, int tenantId) {
		this.sessionUUID = sessionUUID;
		this.userId = userId;
		this.tenantId = tenantId;
	}
	
	public String getSessionUUID() {
		return sessionUUID;
	}

	public int getUserId() {
		return userId;
	}

	public int getTenantId() {
		return tenantId;
	}
	
	public static Integer getCurrentTenant() {
		Integer currentTenantId = -1;
		SessionContext ctx = SessionContext.getContext();
		if (null != ctx) {
			currentTenantId = ctx.getTenantId();
		}
		return currentTenantId;
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public Object get(String key, Object defaultValue) {
		if(map.containsKey(key)) return map.get(key);
		
		return defaultValue;
	}

	public String getString(String key) {
		return (String) map.get(key);
	}

	public String getString(String key, String defaultValue) {
		return map.containsKey(key) ? getString(key) : defaultValue;
	}
	
	public boolean getBoolean(String key) {
		return (Boolean) map.get(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return map.containsKey(key) ? getBoolean(key) : defaultValue;
	}

	public int getInt(String key) {
		return (Integer) map.get(key);
	}
	
	public int getInt(String key, int defaultValue) {
		return map.containsKey(key) ? getInt(key) : defaultValue;
	}
}
