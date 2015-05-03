package com.jay.mvc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Entity
@Table(name = "SYS_SEQUENCE")
public class Sequence {

	private Integer id;
	private Integer tenantId;
	private String sequenceKey;
	private Integer value;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TENANT_ID", nullable = false, updatable = false)
	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}
	
	@Column(name = "SEQUENCE_KEY", nullable = false, updatable = false, length = 32)
	public String getSequenceKey() {
		return sequenceKey;
	}

	public void setSequenceKey(String sequenceKey) {
		this.sequenceKey = sequenceKey;
	}

	@Column(name = "VALUE")
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
