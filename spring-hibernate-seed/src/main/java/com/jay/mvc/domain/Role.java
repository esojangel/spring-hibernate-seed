package com.jay.mvc.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends Abstract {

	private Integer roleId;
	
	private String name;
	
	private List<User> users;
	
	private List<Privilege> privileges;

	@Column(name = "ROLE_ID")
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "NAME",length = 32, unique = false, nullable = false, updatable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 拥有此角色的用户
	 * 
	 * @return
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	/**
	 * 拥有此角色的权限
	 * 
	 * @return
	 */
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "SYS_ROLE_PRIVILEGE", joinColumns = @JoinColumn(name = "ROLE_UUID"), inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_UUID"))
	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}
}
