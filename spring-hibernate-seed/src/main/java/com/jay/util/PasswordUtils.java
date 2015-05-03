package com.jay.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.jay.web.security.AuthSaltSource;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class PasswordUtils {
	public static String SALT = null;
	
	public static String getSalt() {
		if (SALT==null){
			AuthSaltSource saltSource = AppContextUtils.getBean("saltSource");
			SALT = saltSource.getKey();
			
			if(SALT==null)
				SALT = "";
		}
		return SALT;
	}
	
	public static String encode(String password) {
		return new Md5PasswordEncoder().encodePassword(password, getSalt());
	}
	
	public static boolean isValid(String encPass, String rawPass) {
		return new Md5PasswordEncoder().isPasswordValid(encPass, rawPass, getSalt());
	}
}
