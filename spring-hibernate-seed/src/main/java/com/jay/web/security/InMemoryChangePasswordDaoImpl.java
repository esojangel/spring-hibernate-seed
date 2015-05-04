package com.jay.web.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.memory.InMemoryDaoImpl;

/**
 * 
 * @author Jay Zhang
 * 
 */
@SuppressWarnings("deprecation")
public class InMemoryChangePasswordDaoImpl extends InMemoryDaoImpl implements
		IChangePassword {

	public InMemoryChangePasswordDaoImpl() {
		super();
	}

	@Override
	public void changePassword(String username, String password) {
		User oldUser = (User) getUserMap().getUser(username);
		User newUser = new User(oldUser.getUsername(), password,
				oldUser.isEnabled(), oldUser.isAccountNonExpired(),
				oldUser.isCredentialsNonExpired(),
				oldUser.isAccountNonLocked(), oldUser.getAuthorities());
		getUserMap().addUser(newUser);
	}
}
