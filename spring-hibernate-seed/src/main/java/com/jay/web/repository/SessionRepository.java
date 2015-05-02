package com.jay.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.jay.web.domain.Session;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface SessionRepository extends CrudRepository<Session, String> {

}
