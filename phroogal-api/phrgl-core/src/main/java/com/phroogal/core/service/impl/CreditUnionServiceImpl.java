package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.CreditUnion;
import com.phroogal.core.repository.CreditUnionRepository;
import com.phroogal.core.service.CreditUnionService;

/**
 * Default implementation of the {@link CreditUnion} interface
 * @author Christopher Mariano
 *
 */
@Service
public class CreditUnionServiceImpl extends BaseService<CreditUnion, ObjectId, CreditUnionRepository> implements CreditUnionService{

	@Autowired
	private CreditUnionRepository creditUnionRepository;
	
	@Override
	protected CreditUnionRepository getRepository() {
		return creditUnionRepository;
	}	
	
	@Override
	public List<CreditUnion> searchByName(String keyword) {
	    return creditUnionRepository.findByNameLike(keyword);
	}
}
