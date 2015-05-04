package com.jay.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.jay.mvc.domain.User;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class PasswordUtils {
	
	public static String getSalt(User user) {
		return user.getUUID();
	}
	
	public static String encode(String rawPass,String salt) {
		return new Md5PasswordEncoder().encodePassword(rawPass, salt);
	}
	
	public static boolean isValid(String encPass, String rawPass, String salt) {
		return new Md5PasswordEncoder().isPasswordValid(encPass, rawPass, salt);
	}
}
