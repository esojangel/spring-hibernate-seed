package com.jay.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 
 * @author Jay Zhang
 * 
 */
@Entity
@Table(name = "SYS_SESSION")
public class Session extends Abstract {

	private String ip;
	private String deviceType;
	private String deviceId;
	private String deviceOS;
	private String deviceOSVersion;
	private Date lastLoginTime;
	private User user;

	/**
	 * 登陆人IP
	 */
	@Column(name = "IP", length = 20, nullable = false, updatable = false)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 登陆设备类型
	 */
	@Column(name = "DEVICE_TYPE", length = 16, nullable = false, updatable = false)
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * 设备ID
	 */
	@Column(name = "DEVICE_ID", length = 32, nullable = false, updatable = false)
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 设备操作系统
	 */
	@Column(name = "DEVICE_OS", length = 16, nullable = false, updatable = false)
	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	/**
	 * 设备操作系统版本
	 */
	@Column(name = "DEVICE_OS_VERSION", length = 16, nullable = false, updatable = false)
	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	public void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}

	/**
	 * 最后登陆时间
	 */
	@Column(name = "LAST_LOGIN_TIME")
	@Temporal(value = TemporalType.TIME)
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "USER_UUID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
