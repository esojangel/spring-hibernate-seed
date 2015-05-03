package com.jay.mvc.repository;

import org.springframework.data.repository.CrudRepository;

import com.jay.mvc.domain.Session;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface SessionRepository extends CrudRepository<Session, String> {

}
