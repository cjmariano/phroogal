package com.phroogal.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.PasswordResetRequest;
import com.phroogal.core.exception.PasswordResetLinkIsExpiredException;
import com.phroogal.core.exception.PasswordResetRequestIsNotValid;
import com.phroogal.core.exception.UserEmailNotFoundException;
import com.phroogal.core.repository.PasswordResetRequestRepository;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.PasswordResetService;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class PasswordResetServiceImplTest extends BaseServiceTest<PasswordResetRequest, ObjectId, PasswordResetRequestRepository> {
	
	@Autowired
	private PasswordResetService serviceImpl;
	
	@Autowired
	private UserService userServiceImpl;
	
	private UserRepository userRepository;
	
	private PasswordResetRequest request;
	
	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	@Value("${mail.password.reset.validity.hours}")
	private int passwordLinkValidity;
	
	@Override
	protected Service<PasswordResetRequest, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}

	@Override
	protected PasswordResetRequestRepository returnMongoRepository() {
		return Mockito.mock(PasswordResetRequestRepository.class);
	}

	@Override
	protected PasswordResetRequest returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		return generator.generateTestPasswordResetRequest();
	}
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		userRepository = mock(UserRepository.class);
		request = Mockito.mock(PasswordResetRequest.class);
		ReflectionTestUtils.setField(userServiceImpl, "userRepository", userRepository);
		ReflectionTestUtils.setField(serviceImpl, "userService", userServiceImpl);
	}

	@Test
	@Override
	public void testFindById() {
		when(getRepository().findOne(null)).thenReturn(request);
		when(request.getRequestDate()).thenReturn(new DateTime());
		super.testFindById();
	}
	
	@Test(expected=UserEmailNotFoundException.class)
	public void testGivenEmailIsNotValid() throws Exception {
		String invalidEmail = "a@b.com";
		when(userRepository.findByProfileEmail((invalidEmail))).thenReturn(null);
		serviceImpl.createPasswordResetRequest(invalidEmail);
	}
	
	@Test
	public void testPasswordLinkIsValid() throws Exception {
		DateTime requestDate = new DateTime();
		requestDate = requestDate.minusHours(passwordLinkValidity - 2);
		when(getRepository().findOne(null)).thenReturn(request);
		when(request.getRequestDate()).thenReturn(requestDate);
		serviceImpl.findById(null);
	}
	
	@Test
	@Ignore //TODO Why is this ignored? Continue this
	public void testProcessResetRequest() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		User user = generator.generateTestUser();
		PasswordResetRequest request = returnDomainInstance();
		ObjectId requestId = request.getId();
		String newPassword = "newPassword";
		userServiceImpl.saveOrUpdate(user);
		when(getRepository().findOne(requestId)).thenReturn(request);
		when(userRepository.findOne(user.getId())).thenReturn(user);
		
		serviceImpl.processResetRequest(requestId, newPassword);
		Assert.assertTrue(passwordEncoder.matches(newPassword, user.getProfile().getPassword()));
	}
	
	@Test(expected=PasswordResetLinkIsExpiredException.class)
	public void testPasswordLinkIsNotValid() throws Exception {
		DateTime requestDate = new DateTime();
		requestDate = requestDate.minusHours(passwordLinkValidity + 2);
		when(getRepository().findOne(null)).thenReturn(request);
		when(request.getRequestDate()).thenReturn(requestDate);
		serviceImpl.findById(null);
	}
	
	@Test(expected=PasswordResetRequestIsNotValid.class)
	public void testRequestIsNotValid() throws Exception {
		when(getRepository().findOne(null)).thenReturn(null);
		serviceImpl.findById(null);
	}
}
