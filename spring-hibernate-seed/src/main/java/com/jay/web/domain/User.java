package com.jay.web.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Entity
@Table(name = "SYS_USER")
public class User extends Tenant {

	private Boolean active = true;
	private Integer userId;
	private String loginName;
	private String password;
	private String name;
	private Integer gender = 0;
	private Integer age = 25;
	private String mobile;
	private String comment;
	private List<Role> roles;
	private List<Session> sessions;

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uuid", getUUID());
		map.put("name", name);
		map.put("loginName", loginName);
		map.put("gender", gender);
		map.put("age", age);
		map.put("mobile", mobile);
		map.put("comment", comment);
		return map;
	}

	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "USER_ID")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	// 登陆名 loginName
	@NotNull(message = "{username.not.empty}")
	@Pattern(regexp = "^[a-zA-Z_][\\w]{4,19}$", message = "{user.name.error}")
	@Length(min = 5, max = 20, message = "{user.name.length.error}")
	@Column(name = "LOGIN_NAME", length = 16, unique = true, nullable = false, updatable = true)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	// 密码 password
	@Column(name = "PASSWORD", unique = false, nullable = false, updatable = true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 姓名 name
	@Column(name = "NAME", length = 16, unique = false, nullable = false, updatable = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 性别 gender 0:女 1:男
	@Column(name = "GENDER")
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	// 年龄 age
	@Column(name = "AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// 手机 mobile
	@Column(name = "MOBILE", length = 16, unique = false, nullable = false, updatable = true)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// 备注 comment
	@Length(max = 512, message = "{user.comment.length.error}")
	@Column(name = "COMMENT", length = 512, unique = false, nullable = false, updatable = true)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 拥有的角色
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SYS_ROLE_USER", joinColumns = @JoinColumn(name = "USER_UUID"), inverseJoinColumns = @JoinColumn(name = "ROLE_UUID"))
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 登陆记录
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	@Transient
	public boolean isAdmin() {
		boolean flag = false;
		List<Role> roles = getRoles();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				if (role.getRoleId().equals(0)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
}
