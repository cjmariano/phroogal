package com.phroogal.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.EmailConfirmationRequest;
import com.phroogal.core.exception.EmailConfirmationRequestIsNotValid;
import com.phroogal.core.exception.UserEmailNotFoundException;
import com.phroogal.core.repository.EmailConfirmationRequestRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.EmailConfirmationService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class EmailConfirmationServiceImplTest extends BaseServiceTest<EmailConfirmationRequest, ObjectId, EmailConfirmationRequestRepository> {
	
	@Autowired
	private EmailConfirmationService serviceImpl;
	
	@Autowired
	private UserService userServiceImpl;
	
	private UserRepository userRepository;
	
	private EmailConfirmationRequest request;
	
	@Override
	protected Service<EmailConfirmationRequest, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected EmailConfirmationRequestRepository returnMongoRepository() {
		return Mockito.mock(EmailConfirmationRequestRepository.class);
	}

	@Override
	protected EmailConfirmationRequest returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestEmailRepositoryRequest();
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		userRepository = mock(UserRepository.class);
		request = Mockito.mock(EmailConfirmationRequest.class);
		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);
		ReflectionTestUtils.setField(serviceImpl, "userService", userServiceImpl);
	}

	@Test
	@Override
	public void testFindById() {
		when(getRepository().findOne(null)).thenReturn(request);
		super.testFindById();
	}
	
	@Test(expected=UserEmailNotFoundException.class)
	public void testGivenEmailIsNotValid() throws Exception {
		String invalidEmail = "a@b.com";
		when(userRepository.findByProfileEmail((invalidEmail))).thenReturn(null);
		serviceImpl.createRequest(invalidEmail);
	}
	
	@Test
	public void testProcessRequest() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		User user = generator.generateTestUser();
		EmailConfirmationRequest request = returnDomainInstance();
		ObjectId requestId = request.getId();
		userServiceImpl.saveOrUpdate(user);
		when(getRepository().findOne(requestId)).thenReturn(request);
		when(userRepository.findByUsername("a@b.com")).thenReturn(user);
		
		serviceImpl.processRequest(requestId);
		Assert.assertTrue(user.isEmailVerified());
	}
	
	@Test(expected=EmailConfirmationRequestIsNotValid.class)
	public void testRequestIsNotValid() throws Exception {
		when(getRepository().findOne(null)).thenReturn(null);
		serviceImpl.findById(null);
	}
	
	@Test
	public void testRetrieveExistingConfirmationRequest() {
		String testEmail = "a@b.com"; 
		TestEntityGenerator generator = new TestEntityGenerator();
		User user = generator.generateTestUser();
		when(userRepository.findByUsername(testEmail)).thenReturn(user);
		
		EmailConfirmationRequest request = serviceImpl.createRequest(testEmail);
		Assert.assertNotNull(request);
		Assert.assertTrue(request.getEmail().equals(testEmail));
	}
	
	@Test
	public void testRecreateConfirmationRequest() {
		TestEntityGenerator generator = new TestEntityGenerator();
		String testEmail = "a@b.com";
		User user = generator.generateTestUser();
		user.getProfile().setEmail(testEmail);
		userServiceImpl.saveOrUpdate(user);
		 
		when(userRepository.findByUsername(testEmail)).thenReturn(user);
		EmailConfirmationRequest request = serviceImpl.createRequest(testEmail);
		Assert.assertNotNull(request);
	}
	
	@Test(expected=UserEmailNotFoundException.class)
	public void testRecreateConfirmationRequestFail_UsernameNotFound() {
		TestEntityGenerator generator = new TestEntityGenerator();
		String testEmail = "a@b.com";
		User user = generator.generateTestUser();
		user.getProfile().setEmail(testEmail);
		userServiceImpl.saveOrUpdate(user);
		 
		when(userRepository.findByProfileEmail(testEmail)).thenReturn(null);
		serviceImpl.createRequest(testEmail);
	}
}
