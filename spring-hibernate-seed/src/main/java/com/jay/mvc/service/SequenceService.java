package com.jay.mvc.service;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jay.mvc.domain.Sequence;
import com.jay.util.SessionContext;

/**
 * 
 * @author Jay Zhang
 *
 */
@Service
public class SequenceService{

	@PersistenceContext
	public EntityManager manager;

	@Transactional
	public int getNextSequenceNumber(String key) {
		Integer tenantId = SessionContext.getCurrentTenant();
		
		String sql = "UPDATE SYS_SEQUENCE SET VALUE = LAST_INSERT_ID(VALUE + 1) WHERE TENANT_ID = :tenantId and SEQUENCE_KEY = :sequenceKey";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("sequenceKey", key);
		query.setParameter("tenantId", tenantId);
		int updated = query.executeUpdate();
		
		if(updated == 1) {
			sql = "SELECT LAST_INSERT_ID()";
			query = manager.createNativeQuery(sql);
			BigInteger seq = (BigInteger) query.getSingleResult();
			return seq.intValue();
		}
		else {
			Sequence sequence = new Sequence();
			sequence.setSequenceKey(key);
			sequence.setValue(1);
			sequence.setTenantId(tenantId);
			manager.persist(sequence);
			return 1;
		}
	}
}
