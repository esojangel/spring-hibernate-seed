package com.jay.web.repository;

import org.springframework.data.repository.CrudRepository;

import com.jay.web.domain.Sequence;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface SequenceRepository extends CrudRepository<Sequence, Integer> {

	Sequence findBySequenceKey(String key);
	
}
