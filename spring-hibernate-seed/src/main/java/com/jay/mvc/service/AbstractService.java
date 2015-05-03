package com.jay.mvc.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.jay.mvc.domain.Abstract;
import com.jay.util.EntityUtils;

/**
 * 
 * @author Jay Zhang
 *
 */
public abstract class AbstractService<T extends Abstract> {
	
	@PersistenceContext
	public EntityManager manager;

	@Transactional
	public T create(T entity) {
		manager.persist(entity);
		return entity;
	}

	@Transactional
	public T update(T entity) {
		EntityUtils.checkSecurity(manager,entity);
		return manager.merge(entity);
	}

	@Transactional
	public void delete(final T entity) {
		T old = EntityUtils.checkSecurity(manager,entity);
		manager.remove(old);
	}
	
	@Transactional
	public void delete(String uuid) {
		T old = find(uuid);
		delete(old);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Transactional
	public T find(String uuid) {
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType p=(ParameterizedType)type;
		Class<T> claze=(Class<T>) p.getActualTypeArguments()[0];
		return manager.find(claze, uuid);
	}
	
	@Transactional
	public <E extends Abstract> E find(Class<E> claze, String uuid) {
		return manager.find(claze, uuid);
	}

	@Transactional
	public <E extends Abstract> List<E> query(Class<E> claze, Integer limit, Integer offset) {
		return EntityUtils.getAllEntities(manager, claze, limit, offset);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Transactional
	public List<T> query(Integer limit, Integer offset) {
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType p=(ParameterizedType)type;
		Class<T> claze=(Class<T>) p.getActualTypeArguments()[0];
		return EntityUtils.getAllEntities(manager, claze, limit, offset);
	}

	@Transactional
	public <E extends Abstract> Integer queryAmount(Class<E> claze) {
		return EntityUtils.getEntityAmount(manager, claze);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Integer queryAmount() {
		Type type = this.getClass().getGenericSuperclass();
		ParameterizedType p=(ParameterizedType)type;
		Class<T> claze=(Class<T>) p.getActualTypeArguments()[0];
		return EntityUtils.getEntityAmount(manager, claze);
	}
}
