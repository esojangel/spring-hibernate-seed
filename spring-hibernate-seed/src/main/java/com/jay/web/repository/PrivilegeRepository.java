package com.jay.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.jay.web.domain.Privilege;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface PrivilegeRepository extends CrudRepository<Privilege, String> {

	Privilege findByPrivilegeId(Integer privilegeId);

	@Query("select p from Privilege p order by point asc")
	List<Privilege> findAllPrivileges();

	List<Privilege> findByCategory(String category);

	List<Privilege> findByPoint(String point);
}
