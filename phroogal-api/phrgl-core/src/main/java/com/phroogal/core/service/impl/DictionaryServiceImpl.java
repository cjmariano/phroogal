package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Dictionary;
import com.phroogal.core.repository.DictionaryRepository;
import com.phroogal.core.service.DictionaryService;

/**
 * Default implementation of the {@link Dictionary} interface
 * @author Christopher Mariano
 *
 */
@Service
public class DictionaryServiceImpl extends BaseService<Dictionary, ObjectId, DictionaryRepository> implements DictionaryService{

	@Autowired
	private DictionaryRepository dictionaryRepository;
	
	@Override
	protected DictionaryRepository getRepository() {
		return dictionaryRepository;
	}	
}
