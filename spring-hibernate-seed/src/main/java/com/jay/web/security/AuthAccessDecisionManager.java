package com.jay.web.security;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("deprecation")
public class AuthAccessDecisionManager extends AffirmativeBased {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null)
			return;
		
		if(authentication.getAuthorities().size()==1){
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if ("ROLE_SUPER_ADMIN".equals(ga.getAuthority())) { // ga is user's role.
					return;
				}
			}
		}

		super.decide(authentication, object, configAttributes);
	}
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
