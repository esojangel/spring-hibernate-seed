package com.jay.web.service;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jay.web.domain.Role;
import com.jay.web.repository.RoleRepository;

/**
 * 
 * @author Jay Zhang
 *
 */
@Service
public class RoleService extends AbstractService<Role>{

	@Autowired
	private RoleRepository repository;
	
	public Role findByRoleId(Integer roleId) {
		return repository.findByRoleId(roleId);
	}

	public Page<Role> getRoles(Integer pageNumber, Integer pageSize) {
		PageRequest request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "roleId");
		return repository.findAll(new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.greaterThan(root.<Integer> get("roleId"), 0);// 0是超级管理员
			}
		}, request);
	}

	@Transactional
	public boolean batchUpdate(String[] roleUuids, String[] privilegeUuids, String[] operations) {
		Query query = null;
		Query qAdd = manager.createNativeQuery("INSERT INTO sys_role_privilege(ROLE_UUID,PRIVILEGE_UUID) VALUES( :role, :privilege)");
		Query qDel = manager.createNativeQuery("DELETE FROM sys_role_privilege WHERE ROLE_UUID= :role AND PRIVILEGE_UUID = :privilege");
		int len = roleUuids.length;
		for (int i = 0; i < len; i++) {
			query = "1".equals(operations[i]) ? qAdd : qDel;
			query.setParameter("role", roleUuids[i]).setParameter("privilege", privilegeUuids[i]).executeUpdate();
		}
		return true;
	}
}
