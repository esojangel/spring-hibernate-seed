package com.jay.mvc.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.jay.util.Base64Utils;
import com.jay.util.EntityUtils;
import com.jay.util.SessionContext;

/**
 * 
 * @author Jay Zhang
 * 
 */
@MappedSuperclass
public abstract class Abstract implements Cloneable {
	public static final byte ACTION_INSERT = 0;
	public static final byte ACTION_UPDATE = 1;
	public static final byte ACTION_DELETE = 2;

	private String uuid;
	private Integer version = 0;
	private Boolean dirty = false;
	private Integer createdBy;
	private Date dateCreated;
	private Integer modifiedBy;
	private Date dateModified;
	private List<Object> extendedData;

	@Id
	@Column(name = "UUID", nullable = false, updatable = false, length = 22, columnDefinition = "CHAR(22) CHARACTER SET 'latin1' COLLATE 'latin1_bin'")
	public String getUUID() {
		return this.uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	@Version
	@Column(name = "VERSION")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transient
	public Boolean isDirty() {
		return this.dirty;
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

	@Transient
	public Object getData() {
		if (this.extendedData == null) {
			return null;
		}
		return this.extendedData.get(0);
	}

	public void setData(Object value) {
		if (this.extendedData == null) {
			this.extendedData = new ArrayList<Object>(1);
		}
		this.extendedData.set(0, value);
	}

	public Object getData(String key) {
		if (this.extendedData == null)
			return null;

		int len = this.extendedData.size();
		for (int i = 1; i < len; i = i + 2) {
			if (this.extendedData.get(i).equals(key)) {
				return this.extendedData.get(i + 1);
			}
		}

		return null;
	}

	public void setData(String key, Object value) {
		if (this.extendedData == null) {
			this.extendedData = new ArrayList<Object>(3);
			this.extendedData.set(1, key);
			this.extendedData.set(2, value);
		} else {
			boolean flag = false;
			int len = this.extendedData.size();
			for (int i = 1; i < len; i = i + 2) {
				if (this.extendedData.get(i).equals(key)) {
					this.extendedData.set(i + 1, value);
					flag = true;
					break;
				}
			}

			if(!flag) {
				this.extendedData.add(key);
				this.extendedData.add(value);
			}
		}
	}

	@PrePersist
	protected void prePersist() {
		if (this.uuid == null) {
			this.uuid = Base64Utils.randomUUID();
		}
		this.version = 1;

		// for date migration
		if (this.dateCreated == null) {
			this.dateCreated = new Date();
		}
		this.dateModified = new Date();

		SessionContext ctx = SessionContext.getContext();
		if (ctx != null) {
			this.createdBy = ctx.getUserId();
			this.modifiedBy = ctx.getUserId();
		}
	}

	@PostPersist
	protected void postPersist() {
		EntityUtils.saveLog(this, ACTION_INSERT);
	}

	@PreUpdate
	protected void preUpdate() {
		this.dateModified = new Date();
		SessionContext ctx = SessionContext.getContext();
		if (ctx != null) {
			this.modifiedBy = ctx.getUserId();
		}
	}

	@PostUpdate
	protected void postUpdate() {
		EntityUtils.saveLog(this, ACTION_UPDATE);
	}

	@PreRemove
	protected void preRemove() {
	}

	@PostRemove
	protected void postRemove() {
		this.dateModified = new Date();
		SessionContext ctx = SessionContext.getContext();
		if (ctx != null) {
			this.modifiedBy = ctx.getUserId();
		}
		EntityUtils.saveLog(this, ACTION_DELETE);
	}

	@PostLoad
	public void postLoad() {
	}

	@Override
	public boolean equals(Object o) {
		if (this.uuid == null)
			return super.equals(o);
		return (o == this || (o instanceof Abstract && this.getUUID().equals(
				((Abstract) o).getUUID())));
	}

	@Override
	public int hashCode() {
		if (this.uuid == null)
			return super.hashCode();
		return getUUID().hashCode();
	}

	/**
	 * Internal use
	 */
	public void copyExtendedData(Abstract another) {
		this.extendedData = another.extendedData;
	}

	public Object clone() throws CloneNotSupportedException {
		Abstract obj = (Abstract) super.clone();
		obj.setUUID(null);
		obj.extendedData = null;
		return obj;
	}
}
