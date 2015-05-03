package com.jay.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jay.util.TxCallable;
import com.jay.util.TxUtils;
import com.jay.web.domain.Privilege;
import com.jay.web.domain.Role;
import com.jay.web.domain.User;

@Service
public class AuthUserDetailsService implements UserDetailsService {

	protected static Logger logger = Logger.getLogger(AuthUserDetailsService.class);

	private String salt = "";
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User u = this.findUserByLoginName(username);
		if (u == null) {
			throw new UsernameNotFoundException("The user " + username + " is not exist.");
		}
		return new AuthUser(u.getLoginName(), u.getPassword(), salt, u.getActive(), getAuthorities(u));
	}

	public User findUserByLoginName(final String loginName) {
		 return TxUtils.callTx(new TxCallable<User>() {

			@SuppressWarnings("unchecked")
			@Override
			public User call(EntityManager manager) throws Exception {
				Query query = manager.createNativeQuery("SELECT * FROM SYS_USER WHERE LOGIN_NAME = :loginName", User.class);
				query.setParameter("loginName", loginName);
				List<User> users = query.getResultList();
				if(!CollectionUtils.isEmpty(users)) {
					User user = users.get(0);
					if(!Hibernate.isInitialized(user.getRoles())) {
						Hibernate.initialize(user.getRoles());
					}
					if(!CollectionUtils.isEmpty(user.getRoles())){
						List<Role> roles = user.getRoles();
						for(Role role : roles) {
							if(Hibernate.isInitialized(role.getPrivileges()))
								continue;
							Hibernate.initialize(role.getPrivileges());
						}
					}
					return user;
				}
				return null;
			}
		});
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
