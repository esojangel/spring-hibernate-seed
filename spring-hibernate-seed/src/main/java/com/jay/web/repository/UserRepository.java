package com.jay.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jay.web.domain.User;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

	User findByLoginName(String loginName);
	
	Page<User> findAll(Specification<User> spec, Pageable pageable);
}
