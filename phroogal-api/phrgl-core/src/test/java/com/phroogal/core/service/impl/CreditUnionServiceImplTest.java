package com.phroogal.core.service.impl;

import org.bson.types.ObjectId;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.phroogal.core.domain.CreditUnion;
import com.phroogal.core.repository.CreditUnionRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.CreditUnionService;
import com.phroogal.core.service.Service;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class CreditUnionServiceImplTest extends BaseServiceTest<CreditUnion, ObjectId, CreditUnionRepository> {

	@Autowired
	private CreditUnionService serviceImpl;
	
	@Override
	protected Service<CreditUnion, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected CreditUnionRepository returnMongoRepository() {
		return Mockito.mock(CreditUnionRepository.class);
	}

	@Override
	protected CreditUnion returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestCreditUnion();
	}

}
