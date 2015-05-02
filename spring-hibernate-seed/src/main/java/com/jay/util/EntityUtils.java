package com.jay.util;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jay.web.domain.Abstract;
import com.jay.web.domain.Auditrial;
import com.jay.web.domain.Tenant;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class EntityUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(EntityUtils.class);
	
	private static final float NUMBER_PRECISION = 0.0001f;
	//1000ms
	private static final long TIME_PRECISION  = 1000L;
	
	public static boolean isDirty(Abstract entity, Abstract old) {
		Class<?> clazz = entity.getClass();
		Method[] methods = clazz.getMethods();
		try {
			for (Method method : methods) {
				if (method.getAnnotation(Column.class) != null) {
					Object oldValue = method.invoke(old);
					Object newValue = method.invoke(entity);
					if (isPropertyChanged(newValue, oldValue)) {
						return true;
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception when checking dirt.", e);
			return true;
		}

		return false;
	}

	public static boolean isPropertyChanged(Object newValue, Object oldValue) {
		if (newValue == oldValue)
			return false;

		if (newValue == null || oldValue == null)
			return true;

		if (!newValue.equals(oldValue)) {
			if (newValue instanceof Float) {
				float newFloat = (Float) newValue;
				float oldFloat = (Float) oldValue;
				float delta = newFloat - oldFloat;
				if (Math.abs(delta) > NUMBER_PRECISION) {
					return true;
				}
			}
			else if (newValue instanceof Double) {
				double newFloat = (Double) newValue;
				double oldFloat = (Double) oldValue;
				double delta = newFloat - oldFloat;
				if (Math.abs(delta) > NUMBER_PRECISION) {
					return true;
				}
			}
			else if (newValue instanceof Date) {
				long newDate = ((Date) newValue).getTime();
				long oldDate = ((Date) oldValue).getTime();
				long delta = newDate - oldDate;
				if (Math.abs(delta) > TIME_PRECISION ) {
					return true;
				}
			}
			else {
				return true;
			}
		}

		return false;
	}
	
	public static <T extends Abstract> T checkSecurity(EntityManager manager, T entity) {
		@SuppressWarnings("unchecked")
		T old = (T) manager.find(entity.getClass(), entity.getUUID());

		if (old == null) {
			String msg = entity.getClass().getSimpleName() + " has client version of " + entity.getVersion() + ". But it has been deleted";
			LOGGER.error(msg);
			throw new AppException(ExceptionCode.OPTIMISTIC_LOCK_DELETE, entity.getClass().getSimpleName(),entity.getVersion());
		}
	
		if(entity instanceof Tenant) {
			Tenant te = (Tenant) entity;
			if(te.getTenantId() == -1) {
				te.setTenantId(((Tenant) old).getTenantId());
			}
		}

		boolean isDirty = EntityUtils.isDirty(entity, old);
		
		SessionContext ctx = SessionContext.getContext();
		if (isDirty && null != ctx && ctx.getTenantId() != 1){
			//only tenant 1 can update on-Tenant
			if (entity instanceof Tenant){
				Tenant ter = (Tenant) old;
				if (!ter.getTenantId().equals(ctx.getTenantId())){
					String msg =  "Tenant security breach on " + EntityUtils.class.getCanonicalName() + ". Trying update other tenant data.";
					LOGGER.error(msg);
					throw new AppException(ExceptionCode.TENANT_SECURITY, entity.getClass().getCanonicalName());
				}
			} else {
				String msg = "Tenant security breach on " + EntityUtils.class.getCanonicalName() + ". Current user is not super admin, trying update system data.";
				LOGGER.error(msg);
				throw new AppException(ExceptionCode.ONLY_SUPER_ADMIN, entity.getClass().getCanonicalName());
			}
		}
		
		return old;
	}
	
	public static <T extends Abstract> void saveLog(T entity,Byte bytes) {	
		try {
			Class<?> logClass = EntityUtils.getLogClass(entity);
			if (logClass != null) {
				Class<?> abstractClass = entity.getClass();
				final Object log = logClass.newInstance();
				
				Method [] methods = abstractClass.getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(Column.class)) {
						if("getId".equals(method.getName())) continue;
						
						try {
							String setMethodName = method.getName();
							Method setMethod = logClass.getMethod(setMethodName, method.getReturnType());
							if (setMethod != null) {
								Object value = method.invoke(entity);
								setMethod.invoke(log,  value);
							}
						}
						catch(Throwable e) {
							LOGGER.error("Error during writing auditrial records", e);
							// ignore and keep going
						}
					}
					else if (method.isAnnotationPresent(JoinColumn.class)) {
						try {
							String setMethodName = method.getName();
							Method setMethod = logClass.getMethod(setMethodName, String.class);
							if (setMethod != null) {
								Object value = method.invoke(entity);
								if(value instanceof Abstract) {
									setMethod.invoke(log,  ((Abstract) value).getUUID());
								}
							}
						}
						catch(Throwable e) {
							LOGGER.error("Error during writing auditrial records", e);
						}
					}
				}
				Auditrial auditrial = (Auditrial)log;
				
				auditrial.setAction(bytes);
				auditrial.setServerAddress(IPUtils.getServerIP());
				
				TxUtils.runTx(new TxRunnable() {
					@Override
					public void run(EntityManager manager) {
						manager.persist(log);
					}
				});
			}
		}
		catch(Throwable e) {
			LOGGER.error("Error during writing auditrial record for " + entity.getClass().getSimpleName(), e);
		}
	}
	
	public static <T extends Abstract> Class<?> getLogClass(T entity) throws ClassNotFoundException {
		String name = entity.getClass().getName();
		String logName = name + "Log";
		Class<?> logClass = Class.forName(logName);
		return logClass;
	}
	
	public static <T extends Abstract> List<T> getAllEntities(EntityManager manager, Class<T> claze) {
		String sql = String.format("SELECT e FROM %s e", claze.getSimpleName());
		if (Tenant.class.isAssignableFrom(claze)){
			sql += String.format(" WHERE %s = %d",Tenant.TENANT_ID, SessionContext.getCurrentTenant());
		}
		return manager.createQuery(sql, claze).getResultList();
	}

	public static <T extends Abstract> List<T> getAllEntities(EntityManager manager,  Class<T> claze, int limit, int offset) {
		String sql = String.format("SELECT e FROM %s e", claze.getSimpleName());
		if (Tenant.class.isAssignableFrom(claze)){
			sql += String.format(" WHERE %s = %d",Tenant.TENANT_ID, SessionContext.getCurrentTenant());
		}
		return manager.createQuery(sql, claze)	
					.setFirstResult(offset).setMaxResults(limit)
					.getResultList();
	}
	
	public static <T extends Abstract> Integer getEntityAmount(EntityManager manager,  Class<T> claze) {
		String sql = String.format("SELECT COUNT(e) FROM %s e", claze.getSimpleName());
		if (Tenant.class.isAssignableFrom(claze)){
			sql += String.format(" WHERE %s = %d",Tenant.TENANT_ID, SessionContext.getCurrentTenant());
		}
		BigInteger result = (BigInteger) manager.createQuery(sql)	
					.getSingleResult();
		return result.intValue();
	}
}
