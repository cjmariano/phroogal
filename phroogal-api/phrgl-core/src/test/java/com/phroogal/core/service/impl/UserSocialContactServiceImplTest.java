package com.phroogal.core.service.impl;


import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.UserSocialContact;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserSocialContactRepository;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserSocialContactService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserSocialContactServiceImplTest extends BaseServiceTest<UserSocialContact, ObjectId, UserSocialContactRepository> {

	@Autowired
	private UserSocialContactService serviceImpl;
	
	@Autowired
	private UserSocialContactRepository userSocialContactRepository;
	
	
	@Override
	protected Service<UserSocialContact, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected UserSocialContactRepository returnMongoRepository() {
		return Mockito.mock(UserSocialContactRepository.class);
	}

	@Override
	protected UserSocialContact returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestUserSocialContact();
	}
	
	@Test
	public void testGetByUserId() {
		ObjectId testId = ObjectId.get();
		serviceImpl = (UserSocialContactService) returnServiceImpl();
		UserSocialContactRepository userSocialContactRepository = returnMongoRepository();
		ReflectionTestUtils.setField(serviceImpl, "userSocialContactRepository", userSocialContactRepository);
		serviceImpl.getByUserId(testId);
		verify(userSocialContactRepository, atLeastOnce()).findByUserId(testId);
	}
}
