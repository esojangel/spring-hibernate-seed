package com.jay.mvc.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Entity
@Table(name = "SYS_PRIVILEGE")
public class Privilege extends Abstract {

	private Integer privilegeId;

	private String name;

	private String category;

	private String shape;

	private String point;

	private String remarks;

	private List<Role> roles;

	@Column(name = "PRIVILEGE_ID")
	public Integer getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Integer priviledgeId) {
		this.privilegeId = priviledgeId;
	}

	@Column(name = "NAME", length = 32, unique = false, nullable = false, updatable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CATEGORY", length = 32, unique = false, nullable = false, updatable = true)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "SHAPE", length = 32, unique = false, nullable = true, updatable = true)
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	@Column(name = "POINT", length = 32, unique = false, nullable = true, updatable = true)
	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	@Column(name = "REMARKS", length = 64, unique = false, nullable = true, updatable = true)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 拥有的角色
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
