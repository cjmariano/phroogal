package com.phroogal.core.validator;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.Post;
import com.phroogal.core.domain.User;
import com.phroogal.core.domain.security.UserRoleType;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class DeletePostsValidatorTest {

	@Autowired
	@Qualifier(value="deletePostValidator")
	private Validator<Post> deletePostValidator;
	
	@Value("${post.delete.validity.minutes}")
	private int postDeletionValidityInMinutes;
	
	@Autowired
	private AuthenticationDetailsService<String> authenticationService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		ReflectionTestUtils.setField(authenticationService, "userRepository", userRepository);
		ReflectionTestUtils.setField(deletePostValidator, "authenticationService", authenticationService);
	}
	
	@Test
	public void testIsValidReturnsFalse() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		Post post = generator.generateTestQuestion();
		
		DateTime dateTimePastDeleteValidity = DateTime.now().minusMinutes(postDeletionValidityInMinutes + 2);
		post.setCreatedOn(dateTimePastDeleteValidity);
		
		assertFalse(deletePostValidator.isValid(post));
	}
	
	@Test
	public void testIsValidReturnsTrue() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		Post post = generator.generateTestQuestion();
		
		DateTime dateTimeWithinDeleteValidity = DateTime.now().minusMinutes(postDeletionValidityInMinutes - 2);
		post.setCreatedOn(dateTimeWithinDeleteValidity);
		
		assertTrue(deletePostValidator.isValid(post));
	}
	
	@Test
	public void testIsValidReturnsTrueIfUserIsAdmin() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		Post post = generator.generateTestQuestion();
		
		DateTime dateTimePastDeleteValidity = DateTime.now().minusMinutes(postDeletionValidityInMinutes + 2);
		post.setCreatedOn(dateTimePastDeleteValidity);
		
		setAuthenticationPrincipal("ADMIN");
		assertTrue(deletePostValidator.isValid(post));
	}
	
	@Test
	public void testIsValidReturnsFalseIfUserIsNotAdmin() throws Exception {
		TestEntityGenerator generator = new TestEntityGenerator();
		Post post = generator.generateTestQuestion();
		
		DateTime dateTimePastDeleteValidity = DateTime.now().minusMinutes(postDeletionValidityInMinutes + 2);
		post.setCreatedOn(dateTimePastDeleteValidity);
		
		setAuthenticationPrincipal("USER");
		assertFalse(deletePostValidator.isValid(post));
	}
	
	private void setAuthenticationPrincipal(String role) {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		User user = generator.generateTestUser();
		List<UserRoleType> authorities = CollectionUtil.arrayList();
		authorities.add(UserRoleType.get(role));
		user.setGrantedAuthorities(authorities);
		UserCredentials credentials  = new UserCredentials("username", "password");
        Authentication auth = new UsernamePasswordAuthenticationToken(user, credentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        userRepository.save(user);
	}

}
