package com.phroogal.core.service.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.phroogal.core.domain.User;
import com.phroogal.core.domain.UserSocialConnection;
import com.phroogal.core.exception.InvalidPasswordException;
import com.phroogal.core.exception.UserEmailNotFoundException;
import com.phroogal.core.repository.TestEntityGenerator;
import com.phroogal.core.repository.UserRepository;
import com.phroogal.core.repository.UserSocialConnectionRepository;
import com.phroogal.core.service.Service;
import com.phroogal.core.service.UserService;
import com.phroogal.core.social.SocialNetworkType;
import com.phroogal.core.social.connect.MongoUsersConnectionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/app-context.xml"})
public class UserServiceImplTest extends BaseServiceTest<User, ObjectId, UserRepository> {

	@Autowired
	private UserService serviceImpl;
	
	private UserRepository mockedUserRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;
	
	private User user;
	
	@Override
	protected Service<User, ObjectId> returnServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	protected UserRepository returnMongoRepository() {
		mockedUserRepository = mock(UserRepository.class);
		return mockedUserRepository;
	}
	
	@Override
	protected User returnDomainInstance() {
		TestEntityGenerator generator = new TestEntityGenerator(); 
		user = generator.generateTestUser();
		return user;
	}

	@Test
	public void testGetUserByUserName() {
		ReflectionTestUtils.setField(serviceImpl, "userRepository", mockedUserRepository);
		when(mockedUserRepository.findByUsername("userName")).thenReturn(new User());
		serviceImpl.getUserByUserName("userName");
		verify(mockedUserRepository, atLeastOnce()).findByUsername("userName");
	}
	
	@Test
	public void testIsEmailAssociatedWithSocialLogin() {
		User user = new User();
		user.setPrimarySocialNetworkConnection(SocialNetworkType.FACEBOOK);
		ReflectionTestUtils.setField(serviceImpl, "userRepository", mockedUserRepository);
		when(mockedUserRepository.findByProfileEmail("a@b.com")).thenReturn(Arrays.asList(user));
		boolean result = serviceImpl.isEmailAssociatedWithSocialLogin("a@b.com");
		Assert.assertTrue(result);
		
		user.setPrimarySocialNetworkConnection(null);
		when(mockedUserRepository.findByProfileEmail("a@b.com")).thenReturn(Arrays.asList(user));
		result = serviceImpl.isEmailAssociatedWithSocialLogin("a@b.com");
		Assert.assertFalse(result);
	}
	
	@Test
	public void testLoadByUserNameSuccess() {
		UserRepository repositoryMock = mock(UserRepository.class);
		user.setPrimarySocialNetworkConnection(null);
		when(repositoryMock.findByUsername(anyString())).thenReturn(user);
		ReflectionTestUtils.setField(serviceImpl, "userRepository", repositoryMock);
		UserDetails user = ((UserDetailsService)serviceImpl).loadUserByUsername(anyString());
		Assert.assertNotNull(user);
	}
	
	@Test(expected=UserEmailNotFoundException.class)
	public void testLoadByUserNameUserNotFound() {
		UserRepository repositoryMock = mock(UserRepository.class);
		when(repositoryMock.findByProfileEmail(anyString())).thenReturn(null);
		ReflectionTestUtils.setField(serviceImpl, "userRepository", repositoryMock);
		((UserDetailsService)serviceImpl).loadUserByUsername(anyString());
		verify(repositoryMock, atLeastOnce()).findByProfileEmail(anyString());
	}
	
	@Test
	public void testInitializeUserFromConnection() {
		ObjectId userId = new ObjectId("51f1ca124d7ad8e29cf2698b");
		UserSocialConnectionRepository userSocialConnectionRepository = mock(UserSocialConnectionRepository.class);
		MongoUsersConnectionRepository usersConnectionRepository = mock(MongoUsersConnectionRepository.class);
		ReflectionTestUtils.setField(serviceImpl, "usersConnectionRepository", usersConnectionRepository);
		Connection<?> connection = mock(Connection.class);
		UserSocialConnection userConnection = mock(UserSocialConnection.class);
		
		ConnectionData data = new ConnectionData("facebook", userId.toString(), "John", "url", "imageUrl", "accessToken", "secret", "refreshToken", 10L);
		ConnectionKey connectionKey = new ConnectionKey("facebook",userId.toString());
		User mockUser = mock(User.class);
		UserProfile userProfile = mock(UserProfile.class);

		when(connection.createData()).thenReturn(data);
		when(connection.getKey()).thenReturn(connectionKey);
		when(connection.fetchUserProfile()).thenReturn(userProfile);
		when(userSocialConnectionRepository.findByProviderIdAndProviderUserId("facebook", "51f1ca124d7ad8e29cf2698b")).thenReturn(Arrays.asList(new UserSocialConnection[] {userConnection}));
		when(userConnection.getUserId()).thenReturn(userId);
		when(usersConnectionRepository.findUserIdsWithConnection(connection)).thenReturn(Arrays.asList(userId.toString()));
		when(getRepository().findOne(userId)).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(userId);
		
		User user = serviceImpl.getUserFromConnection(connection);
		Assert.assertEquals(user.getId(), userId);
		Assert.assertNotNull(user);
	}
	
	@Test
	public void testChangePassword() {
		ReflectionTestUtils.setField(serviceImpl, "repository", userRepository);
		String oldPassword = "oldPassword";
		String newPassword = "newPassword";
		
		User user = returnDomainInstance();
		user.getProfile().setPassword(passwordEncoder.encode(oldPassword));
		userRepository.save(user);
		serviceImpl.changePassword(user.getId(), oldPassword, newPassword);
		
		User updatedUser = userRepository.findOne(user.getId());
		Assert.assertTrue(passwordEncoder.matches(newPassword, updatedUser.getProfile().getPassword())); 
		
	}
	
	@Test(expected=InvalidPasswordException.class)
	public void testChangePasswordFail() {
		ReflectionTestUtils.setField(serviceImpl, "repository", userRepository);
		String oldPassword = "oldPassword";
		String newPassword = "newPassword";
		
		User user = returnDomainInstance();
		user.getProfile().setPassword(passwordEncoder.encode(oldPassword + "-modified"));
		userRepository.save(user);
		serviceImpl.changePassword(user.getId(), oldPassword, newPassword);
	}
}
