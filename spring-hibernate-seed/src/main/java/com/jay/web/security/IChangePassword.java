package com.jay.web.security;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface IChangePassword extends UserDetailsService {
	void changePassword(String username, String password);
}
