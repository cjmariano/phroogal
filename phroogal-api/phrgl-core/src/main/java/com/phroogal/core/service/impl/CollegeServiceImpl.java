package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.College;
import com.phroogal.core.repository.CollegeRepository;
import com.phroogal.core.service.CollegeService;

/**
 * Default implementation of the {@link College} interface
 * @author Christopher Mariano
 *
 */
@Service
public class CollegeServiceImpl extends BaseService<College, ObjectId, CollegeRepository> implements CollegeService{

	@Autowired
	private CollegeRepository collegeRepository;
	
	@Override
	protected CollegeRepository getRepository() {
		return collegeRepository;
	}
	@Override
	public List<College> findByNameContaining(String keyword) {
	    return collegeRepository.findByNameLike(keyword);
	}
}
