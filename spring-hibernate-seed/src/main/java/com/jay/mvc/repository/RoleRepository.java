package com.jay.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jay.mvc.domain.Role;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, String>,JpaSpecificationExecutor<Role> {

	Role findByRoleId(Integer roleId);
	
	Page<Role> findAll(Specification<Role> spec, Pageable pageable);
}
