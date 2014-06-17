/**
 * 
 */
package com.phroogal.core.service.impl;

import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.exception.InvalidUserNamePasswordException;
import com.phroogal.core.exception.LoginFailedException;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.valueobjects.UserCredentials;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserNamePasswordValidationServiceTest {

	private UserCredentials userCredentials;

	private User user;
	
	private AuthenticationDetailsService<ObjectId> authenticationService;
	
	private AuthenticationManager authenticationManager;
	
	private Authentication authentication;
	
	@Autowired
	private UserNamePasswordValidationService validationService;
	
	private static final String PASSWORD = "pa$$w0rd";
	
	private static final String USERNAME = "user1";
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		user = generator.generateTestUser();
		user.getProfile().setPassword(PASSWORD);
		userCredentials = Mockito.mock(UserCredentials.class);
		authenticationManager = Mockito.mock(AuthenticationManager.class);
		authenticationService = Mockito.mock(AuthenticationDetailsService.class);
		authentication = Mockito.mock(Authentication.class);
		ReflectionTestUtils.setField(validationService, "authenticationManager", authenticationManager);
		ReflectionTestUtils.setField(validationService, "authenticationService", authenticationService);
	}
	
	@Test
	public void testValidValidateCredentials() throws Throwable {
		when(authenticationService.getAuthenticatedUser()).thenReturn(user);
		when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getPrincipal()).thenReturn(user);
		Assert.assertNotNull(validationService.validateCredentials(userCredentials));
	}
	
	@Test(expected=InvalidUserNamePasswordException.class)
	public void testInvalidPassword() throws Throwable {
		when(userCredentials.getPassword()).thenReturn(PASSWORD + "123");
		validationService.validateCredentials(userCredentials);
	}
	
	@Test(expected=InvalidUserNamePasswordException.class)
	public void testInvalidUserName() throws Throwable {
		when(userCredentials.getUsername()).thenReturn(USERNAME);
		when(userCredentials.getPassword()).thenReturn(PASSWORD);
		validationService.validateCredentials(userCredentials);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=LoginFailedException.class)
	public void testUserFailedToLogin() throws Throwable {
		when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(Exception.class);
		validationService.validateCredentials(userCredentials);
	}
}
