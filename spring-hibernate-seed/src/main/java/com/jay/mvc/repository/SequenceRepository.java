package com.jay.mvc.repository;

import org.springframework.data.repository.CrudRepository;

import com.jay.mvc.domain.Sequence;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface SequenceRepository extends CrudRepository<Sequence, Integer> {

	Sequence findBySequenceKey(String key);
	
}
