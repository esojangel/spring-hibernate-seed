package com.jay.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jay.mvc.domain.Session;
import com.jay.mvc.repository.SessionRepository;

/**
 * 
 * @author Jay Zhang
 *
 */
public class SessionService extends AbstractService<Session>{

	@Autowired
	private SessionRepository repository;
	
	@Transactional
	public void save(Session entity) {
		repository.save(entity);
	}

	public Session getSession(String key) {
		return repository.findOne(key);
	}

}
