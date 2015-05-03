/**
 * Copyright (c) 2011 WallTech Corporation.
 * All rights reserved.
 */
package com.jay.mvc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Jay Zhang
 * 
 */
@MappedSuperclass
public abstract class Auditrial {
	private Integer id;
	private String uuid;
	private Integer version;
	private Byte action;
	private String sessionId;
	private String serverAddress;
    private Integer createdBy;
	private Date dateCreated;
	private Integer modifiedBy;
	private Date dateModified;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	public Integer getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "UUID", unique = false, nullable = true, updatable = false, length = 32)
	public String getUUID() {
		return this.uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "VERSION")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "ACTION")
	public Byte getAction() {
		return action;
	}

	public void setAction(Byte action) {
		this.action = action;
	}

	@Column(name = "SESSION_ID", length=22)
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Column(name = "CREATED_BY")
	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "DATE_CREATED")
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "MODIFIED_BY")
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "DATE_MODIFIED")
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	@Column(name = "SERVER_ADDRESS", length=16)	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
}
