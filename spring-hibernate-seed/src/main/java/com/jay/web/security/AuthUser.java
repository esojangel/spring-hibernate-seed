package com.jay.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User {

	private static final long serialVersionUID = 3284703839976496952L;
	private String salt = "";

	public AuthUser(String username, String password, String salt,
			boolean enabled, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, enabled, enabled, enabled,authorities);
		this.salt = salt;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
