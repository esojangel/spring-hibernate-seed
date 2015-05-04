package com.jay.mvc.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.jay.util.Base64Utils;
import com.jay.util.EntityUtils;
import com.jay.util.PasswordUtils;
import com.jay.util.TxCallable;
import com.jay.util.TxUtils;

/**
 * 
 * @author Jay Zhang
 *
 */
@Service
public class UserService extends AbstractService<User>{

	@Autowired
	private UserRepository repository;
	
	@Transactional
	public User create(User entity) {
		entity.setUUID(Base64Utils.randomUUID());
		String password=PasswordUtils.encode(entity.getPassword(), PasswordUtils.getSalt(entity));
		entity.setPassword(password);
		manager.persist(entity);
		return entity;
	}
	
	@Transactional
	public User update(User entity) {
		EntityUtils.checkSecurity(manager,entity);
		String password=PasswordUtils.encode(entity.getPassword(), PasswordUtils.getSalt(entity));
		entity.setPassword(password);
		return manager.merge(entity);
	}

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
	
	@Cacheable(value="cacheMap", key="#loginName")
	@Transactional(readOnly=true)
	public User findUserWithRoles(final String loginName) {
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
	
	@CacheEvict(value="cacheMap", key="#entity.loginName", beforeInvocation=true)
	@Transactional
	public void delete(User entity) {
		User old = EntityUtils.checkSecurity(manager,entity);
		manager.remove(old);
	}

	@Transactional
	public void delete(String uuid) {
		User user = find(uuid);
		delete(user);
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
