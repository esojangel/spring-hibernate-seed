package com.jay.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import com.jay.mvc.domain.Privilege;
import com.jay.mvc.domain.Role;
import com.jay.mvc.domain.User;
import com.jay.mvc.service.UserService;
import com.jay.util.AppContextUtils;
import com.jay.util.PasswordUtils;

public class AuthUserDetailsService implements UserDetailsService {

	protected static Logger logger = Logger.getLogger(AuthUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserService userService = AppContextUtils.getBean("userService");
		User u = userService.findUserWithRoles(username);
		if (u == null) {
			throw new UsernameNotFoundException("The user " + username + " is not exist.");
		}
		return new AuthUser(u.getLoginName(), u.getPassword(), PasswordUtils.getSalt(u), u.getActive(), getAuthorities(u));
	}
	
	/**
	 * 获得访问角色权限列表
	 * 
	 * @param access
	 * @return
	 */
	public Collection<GrantedAuthority> getAuthorities(User user) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.isAdmin()) {
			if(user.getTenantId()==1) {
				auths.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
			} else {
				auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
		} else {
			Map<String, Boolean> pgMap = new HashMap<String, Boolean>();
			if (!CollectionUtils.isEmpty(user.getRoles())) {
				List<Role> roles = user.getRoles();
				List<Privilege> pgs = null;
				String r;
				for (Role role : roles) {
					pgs = role.getPrivileges();
					if (CollectionUtils.isEmpty(pgs))
						continue;
					for (Privilege pg : pgs) {
						if (!pgMap.containsKey(pg.getUUID())) {
							r = String.format("ROLE_%s_%s", pg.getShape(), pg.getPoint());
							auths.add(new SimpleGrantedAuthority(r));
							pgMap.put(pg.getUUID(), Boolean.TRUE);
						}
					}
				}
			}
		}
		return auths;
	}
}
