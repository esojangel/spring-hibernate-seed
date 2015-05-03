package com.jay.web.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthSaltSource implements SaltSource,InitializingBean{

	private String key = "";
	@Override
	public Object getSalt(UserDetails user) {
		return key;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void afterPropertiesSet() throws Exception {}
}
