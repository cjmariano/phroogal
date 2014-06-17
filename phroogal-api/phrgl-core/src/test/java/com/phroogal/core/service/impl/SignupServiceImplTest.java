package com.phroogal.core.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.service.SignupService;
import com.phroogal.core.service.UserService;
import com.phroogal.core.service.UserTagService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class SignupServiceImplTest {
	
	@Autowired
	private SignupService signupService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private UserService userService;
	
	private UserTagService userTagService;
	
	@Before
	public void setUp() {
		userService = mock(UserService.class);
		userTagService = mock(UserTagService.class);
		ReflectionTestUtils.setField(signupService, "userService", userService);
		ReflectionTestUtils.setField(signupService, "userTagService", userTagService);
	}

	@Test
	public void testSignup() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		User user = generator.generateTestUser();
		String plainTextPassword = user.getPassword();
		when(userService.saveOrUpdate(user)).thenReturn(user);
		
		signupService.signup(user);

		verify(userService, Mockito.atLeastOnce()).saveOrUpdate(user);
		verify(userTagService, Mockito.atLeastOnce()).createDefaultUserTagsFor(user);
		Assert.assertTrue(passwordEncoder.matches(plainTextPassword, user.getPassword()));
	}
}
