package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserProfile;
import com.phroogal.core.exception.UserNotAuthenticatedException;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.service.AuthenticationDetailsService;
import com.phroogal.core.utility.CollectionUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class AuthenticationDetailsServiceImplTest {

	@Autowired
	private AuthenticationDetailsService<String> authenticationService;
	
	private UserRepository userRepository;
	
	private static final String PASSWORD = "pa$$w0rd";
	
	private static final String USERNAME = "user1";
	
	private User user = new User();
	
	private ObjectId userId;
	
	@Before
	public void setUp() throws Exception {
		initializeUser();
		setAuthenticationPrincipal();
		userRepository = Mockito.mock(UserRepository.class);
		ReflectionTestUtils.setField(authenticationService, "userRepository", userRepository);
		Mockito.when(userRepository.findOne(userId)).thenReturn(user);
	}

	private void initializeUser() {
		userId = ObjectId.get();
		user.setId(userId);
		UserProfile profile = new UserProfile();
		profile.setPassword(PASSWORD);
		user.setProfile(profile);
		user.setEnabled(true);
	}

	private void setAuthenticationPrincipal() {
		List<GrantedAuthority> authorities = CollectionUtil.arrayList();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		UserCredentials credentials  = new UserCredentials(USERNAME, PASSWORD);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, credentials, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
	

	@Test
	public void testGetAuthenticatedUserId() {
        String authenticatedUserId = authenticationService.getAuthenticatedUserId();
        Assert.assertEquals(userId.toString(), authenticatedUserId);
	}

	@Test
	public void testGetAuthenticatedUser() {
		User userProfile = authenticationService.getAuthenticatedUser();
		Assert.assertEquals(userId.toString(), userProfile.getId().toString());
	}
	
	@Test
	public void testSetAuthenticatedUser() {
		authenticationService.setAuthenticatedUser(user);
		User userProfile = authenticationService.getAuthenticatedUser();
		Assert.assertEquals(userId.toString(), userProfile.getId().toString());
	}
	
	@Test
	public void testVerifyUserIsAuthenticatedPass() {
		authenticationService.setAuthenticatedUser(user);
		authenticationService.verifyUserIsAuthenticated();
	}
	
	@Test(expected=UserNotAuthenticatedException.class)
	public void testVerifyUserIsAuthenticatedFail() {
		SecurityContextHolder.getContext().setAuthentication(null);
		authenticationService.verifyUserIsAuthenticated();
	}
}
