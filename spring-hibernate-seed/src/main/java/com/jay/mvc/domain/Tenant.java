package com.jay.mvc.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jay.util.SessionContext;

/**
 * 
 * @author Jay Zhang
 * 
 */
@MappedSuperclass
public abstract class Tenant extends Abstract {
	
	public static final String TENANT_ID = "tenantId";

	private static Logger LOGGER = LoggerFactory.getLogger(Tenant.class);

	private Integer tenantId = -1;

	@Column(name = "TENANT_ID", nullable = false, updatable = false)
	public Integer getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}
	
	@PrePersist
	protected void prePersist() {
		super.prePersist();
		if (this.tenantId >= 0) {
			SessionContext ctx = SessionContext.getContext();
			if (ctx == null) {
				LOGGER.error("Tenant security breach on " + this.getClass().getCanonicalName() + ". Missing user tenant ID");
				throw new SecurityException();
			}
		}
		else {
			this.tenantId=SessionContext.getCurrentTenant();
			if (this.tenantId < 0){
				LOGGER.error("Tenant security breach on " + this.getClass().getCanonicalName() + ". Missing set tenant ID");
				throw new SecurityException();
			}
		}
	}
	
}
