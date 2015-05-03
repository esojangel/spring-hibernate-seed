package com.jay.mvc.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jay.mvc.domain.Privilege;
import com.jay.mvc.domain.Role;
import com.jay.mvc.domain.User;
import com.jay.mvc.repository.PrivilegeRepository;

/**
 * 
 * @author Jay Zhang
 *
 */
@Service
public class PrivilegeService extends AbstractService<Privilege>{

	@Autowired
	private PrivilegeRepository repository;

	public List<Privilege> getPrivileges() {
		return repository.findAllPrivileges();
	}
	
	public Privilege findByPrivilegeId(Integer privilegeId) {
		return repository.findByPrivilegeId(privilegeId);
	}

	public List<Privilege> findByCategory(String category) {
		return repository.findByCategory(category);
	}

	public List<Privilege> findByPoint(String point) {
		return repository.findByPoint(point);
	}

	@SuppressWarnings("unchecked")
	public List<Privilege> findByUser(final User user) {
		if (!Hibernate.isInitialized(user.getRoles())) {
			Hibernate.initialize(user.getRoles());
		}
		List<Role> roles = user.getRoles();
		if (!CollectionUtils.isEmpty(roles)) {
			List<String> roleUUIDs = new ArrayList<String>(roles.size());
			for (Role role : roles) {
				roleUUIDs.add(role.getUUID());
			}
			String sql = "SELECT DISTINCT * FROM sys_privilege WHERE UUID IN (SELECT DISTINCT PRIVILEGE_UUID FROM sys_role_privilege WHERE ROLE_UUID IN(:roles))";
			Query query = manager.createNativeQuery(sql, Privilege.class).setParameter("roles", roleUUIDs);
			return query.getResultList();
		}
		return null;
	}
}
