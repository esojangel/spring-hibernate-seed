package com.jay.mvc.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.jay.mvc.domain.Role;
import com.jay.mvc.domain.User;
import com.jay.mvc.repository.UserRepository;

/**
 * 
 * @author Jay Zhang
 *
 */
@Service
public class UserService extends AbstractService<User>{

	@Autowired
	private UserRepository repository;

	@Transactional(readOnly=true)
	public User findByUUID(String uuid) {
		User user = repository.findOne(uuid);
		return user;
	}

	@Transactional(readOnly=true)
	public User findUserByLoginName(String loginName) {
		User user = repository.findByLoginName(loginName);
		return user;
	}

	@Transactional
	public void deleteUserByUUID(String uuid) {
		repository.delete(uuid);
	}

	public Page<User> getUsers(Integer pageNumber, Integer pageSize) {
		PageRequest request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "userId");
		return repository.findAll(new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.notEqual(root.<String> get("loginName"), "admin");// admin是超级管理员
			}
		}, request);
	}

	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}

	public Page<User> findAll(Specification<User> spec, Pageable pageable) {
		return repository.findAll(spec, pageable);
	}

	public Page<User> getUsers(final String name, Integer pageNumber, Integer pageSize) {
		PageRequest request = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.DESC, "dateCreated");
		Page<User> page = repository.findAll(new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(cb.like(root.<String> get("name"), "%" + name.trim() + "%"), cb.notEqual(root.<String> get("loginName"), "admin"));
			}
		}, request);
		return page;
	}
}
