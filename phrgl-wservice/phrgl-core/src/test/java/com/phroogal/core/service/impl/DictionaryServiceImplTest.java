package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.Dictionary;
import com.phroogal.core.repository.DictionaryRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.DictionaryService;
import com.phroogal.core.service.Service;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class DictionaryServiceImplTest extends BaseServiceTest<Dictionary, ObjectId, DictionaryRepository> {

	@Autowired
	private DictionaryService serviceImpl;
	
	@Override
	protected Service<Dictionary, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected DictionaryRepository returnMongoRepository() {
		return Mockito.mock(DictionaryRepository.class);
	}

	@Override
	protected Dictionary returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestDictionary();
	}

}
