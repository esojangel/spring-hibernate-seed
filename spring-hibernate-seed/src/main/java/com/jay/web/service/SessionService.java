package com.jay.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jay.web.domain.Session;
import com.jay.web.repository.SessionRepository;

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
